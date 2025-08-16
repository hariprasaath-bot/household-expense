import { Component, ElementRef, ViewChild, NgZone, OnInit } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { HttpClient, HttpEvent, HttpEventType, HttpParams } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

// PrimeNG Modules
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { ProgressBarModule } from 'primeng/progressbar';
import { Table, TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { CalendarModule } from 'primeng/calendar';
import { DialogModule } from 'primeng/dialog';
import { DropdownModule } from 'primeng/dropdown';
import { TooltipModule } from 'primeng/tooltip';
import { MessageService, SelectItem } from 'primeng/api';
import { UserService } from '../../../core/services/user.service';
import { ToastModule } from 'primeng/toast';
import { Observable, of, forkJoin } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

// Types
import { 
  DateRange, 
  DateFilterType, 
  SSEData, 
  Category, 
  User, 
  Transaction, 
  BatchUpdateModel 
} from './transaction-upload.types';
// Types are now imported from transaction-upload.types.ts

@Component({
  selector: 'app-transaction-upload',
  standalone: true,
  imports: [
    ButtonModule, 
    RippleModule, 
    ProgressBarModule, 
    CommonModule, 
    TableModule, 
    InputTextModule, 
    CalendarModule,
    FormsModule,
    DialogModule,
    DropdownModule,
    TooltipModule,
    ToastModule
  ],
  providers: [MessageService, DatePipe],
  templateUrl: './transaction-upload.component.html',
  styleUrl: './transaction-upload.component.scss'
})
export class TransactionUploadComponent implements OnInit {
  // UI State
  uploadedFiles: File[] = [];
  showProgressBar = false;
  progressValue = 0;
  progressMessage = '';
  isLoading = false;
  isSaving = false;
  
  // View Children
  @ViewChild('uploader') uploader!: ElementRef<HTMLInputElement>;
  @ViewChild('dt') dt!: Table;
  
  // Data
  transactions: Transaction[] = [];
  filteredTransactions: Transaction[] = [];
  selectedTransactions: Transaction[] = [];
  selectedTransaction: Transaction | null = null;
  
  // Filtering
  activeFilter: DateFilterType = null;
  dateRange: DateRange = null;
  
  // Categories
  categories: SelectItem[] = [];
  availableCategories: SelectItem[] = [];
  newCategoryName = '';
  newCategorySearchString = '';
  selectedCategoryType: 'EXPENSE' | 'INCOME' | 'INVESTMENT' = 'EXPENSE';
  showNewCategoryInput = false;
  categoryTypes = [
    { label: 'Expense', value: 'EXPENSE' },
    { label: 'Income', value: 'INCOME' },
    { label: 'Investment', value: 'INVESTMENT' }
  ];
  
  // Forms
  batchUpdateModel: BatchUpdateModel = {
    category: null,
    remark: ''
  };
  
  // UI State
  displayDialog = false;
  constructor(
    private http: HttpClient, 
    private zone: NgZone,
    private messageService: MessageService, 
    private datePipe: DatePipe,
    private userService: UserService
  ) {
    // Initialize with empty arrays to prevent undefined errors
    this.transactions = [];
    this.filteredTransactions = [];
    this.selectedTransactions = [];
    this.loadTransactions();
  }

  ngOnInit(): void {
    this.loadCategories();
    this.loadTransactions();
    this.setDateRange('month'); // Default to showing current month
  }

  setDateRange(rangeType: DateFilterType): void {
    this.activeFilter = rangeType;
    const today = new Date();
    
    switch(rangeType) {
      case 'week': {
        const firstDay = new Date(today);
        firstDay.setDate(today.getDate() - today.getDay()); // Start of current week
        const lastDay = new Date(firstDay);
        lastDay.setDate(firstDay.getDate() + 6); // End of current week
        this.dateRange = [firstDay, lastDay];
        break;
      }
      case 'month': {
        const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);
        const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
        this.dateRange = [firstDay, lastDay];
        break;
      }
      case 'threeMonths': {
        const firstDay = new Date(today.getFullYear(), today.getMonth() - 2, 1);
        const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
        this.dateRange = [firstDay, lastDay];
        break;
      }
      case 'custom':
        // Just show the date picker, don't set any range
        return;
      default:
        this.dateRange = null;
    }
    
    this.applyDateFilter();
  }

  applyDateFilter(): void {
    if (!this.dateRange || !this.dateRange[0] || !this.dateRange[1]) {
      this.filteredTransactions = [...this.transactions];
      return;
    }

    const startDate = new Date(this.dateRange[0]);
    const endDate = new Date(this.dateRange[1]);
    
    // Set time to start and end of day for accurate comparison
    startDate.setHours(0, 0, 0, 0);
    endDate.setHours(23, 59, 59, 999);

    this.filteredTransactions = this.transactions.filter(transaction => {
      const transactionDate = new Date(transaction.transaction_date.$date);
      return transactionDate >= startDate && transactionDate <= endDate;
    });
  }

  clearDateFilter(): void {
    this.dateRange = null;
    this.activeFilter = null;
    this.loadTransactions();
  }

  onDateSelect(event: any): void {
    // This method is triggered when a date is selected in the calendar
    // We'll use it to automatically apply the filter when both dates are selected
    if (this.dateRange && this.dateRange[0] && this.dateRange[1]) {
      this.applyDateFilter();
    }
  }



  // Helper method to handle input events for filtering
  onInput(event: Event, field: string): void {
    const value = (event.target as HTMLInputElement).value;
    if (this.dt) {
      this.dt.filter(value, field, 'contains');
    }
  }

  private loadTransactions(): void {
      
    this.http.get<Transaction[]>('http://localhost:8090/bank/transactions').subscribe({
      next: (transactions) => {
        this.transactions = transactions;
        this.filteredTransactions = [...transactions];
      },
      error: (error) => {
        console.error('Error loading transactions:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to load transactions'
        });
      }
    });
  }

  private handleError(message: string, error: any): void {
    console.error(`${message}:`, error);
    this.messageService.add({
      severity: 'error',
      summary: 'Error',
      detail: message
    });
  }

  private loadCategories(): void {
    try {
      const currentUser = this.userService.getCurrentUser();
      if (!currentUser?.userId) {
        console.warn('User not authenticated or missing userId');
        this.categories = [];
        this.availableCategories = [];
        return;
      }
      
      const userId = currentUser.userId;
      this.isLoading = true;

      forkJoin([
        this.http.get<Category[]>('http://localhost:8090/bank/categories').pipe(
          catchError(error => {
            console.error('Error loading global categories:', error);
            return of<Category[]>([]);
          })
        ),
        this.http.get<Category[]>(`/api/users/${userId}/categories`).pipe(
          catchError(error => {
            console.error('Error loading user categories:', error);
            return of<Category[]>([]);
          })
        )
      ]).pipe(
        map(([globalCategories, userCategories]) => {
          // Process global categories
          const globalItems = (globalCategories || []).map(c => ({
            label: c.name,
            value: c.id,
            isCustom: false
          }));

          // Process user categories
          const userItems = (userCategories || []).map(c => ({
            label: c.name,
            value: c.id,
            isCustom: true
          }));

          // Combine and return
          return [...globalItems, ...userItems];
        })
      ).subscribe({
        next: (allCategories) => {
          this.zone.run(() => {
            this.availableCategories = [...allCategories];
            this.categories = [
              ...allCategories,
              { label: 'Add New Category', value: 'add-new', isCustom: false }
            ];
            this.isLoading = false;
          });
        },
        error: (error) => {
          console.error('Error in category subscription:', error);
          this.zone.run(() => {
            this.handleError('Failed to load categories', error);
            this.isLoading = false;
          });
        }
      });
    } catch (error) {
      console.error('Unexpected error in loadCategories:', error);
      this.zone.run(() => {
        this.handleError('An unexpected error occurred', error);
        this.isLoading = false;
      });
    }
  }

  addNewCategory(): void {
    if (!this.newCategoryName.trim()) return;
    
    
    const newCategory: Omit<Category, 'id'> = {
      name: this.newCategoryName.trim(),
      searchString: this.newCategorySearchString.trim(),
      isCustom: true,
      categoryType: this.selectedCategoryType
    };
    
    this.http.post<Category>('http://localhost:8090/user/category/add', newCategory).subscribe({
      next: (savedCategory) => {
        const categoryOption = {
          label: savedCategory.name,
          value: savedCategory.id,
          isCustom: true
        };
        
        this.availableCategories = [...this.availableCategories, categoryOption];
        this.batchUpdateModel.category = savedCategory.id;
        this.newCategoryName = '';
        this.newCategorySearchString = '';
        this.showNewCategoryInput = false;
        
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Category added successfully'
        });
      },
      error: (error) => {
        console.error('Error adding category:', error);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'Failed to add category'
        });
      }
    });
  }

  onDialogShow(): void {
    this.loadCategories();
    this.showNewCategoryInput = false;
    this.newCategoryName = '';
    this.newCategorySearchString = '';
  }

  onDialogHide(): void {
    this.displayDialog = false;
    this.batchUpdateModel = { category: null, remark: '' };
    this.showNewCategoryInput = false;
  }

  openBatchUpdate(): void {
    if (this.selectedTransactions && this.selectedTransactions.length > 0) {
      this.batchUpdateModel = { category: null, remark: '' };
      this.displayDialog = true;
    }
  }

  submitBatchUpdate(): void {
    if (!this.selectedTransactions || this.selectedTransactions.length === 0) {
      this.messageService.add({ severity: 'warn', summary: 'No transactions selected', detail: 'Please select one or more transactions to update.' });
      return;
    }

    if (!this.batchUpdateModel.category) {
      this.messageService.add({ severity: 'warn', summary: 'No category selected', detail: 'Please select a category.' });
      return;
    }

    this.isSaving = true;
    const transactionIds = this.selectedTransactions.map(t => t.transactionId);
    const payload = {
      transactionIds,
      categoryId: this.batchUpdateModel.category,
      remark: this.batchUpdateModel.remark
    };

    this.http.post('http://localhost:8090/bank/transactions/batch-update', payload).subscribe({
      next: () => {
        this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Transactions updated successfully.' });
        this.displayDialog = false;
        this.isSaving = false;
        this.loadTransactions();
        this.selectedTransactions = [];
      },
      error: (error) => {
        this.isSaving = false;
        this.handleError('Failed to update transactions', error);
      }
    });
  }

  onCategoryChange(event: any): void {
    if (event.value === 'add-new') {
      this.showNewCategoryInput = true;
      this.batchUpdateModel.category = null;
    } else {
      this.showNewCategoryInput = false;
    }
  }

  startProgress(): void {
    if (this.uploader?.nativeElement) {
      this.uploader.nativeElement.click();
    }
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.uploadedFiles = [file];
      this.isLoading = true;
      
      // Reset file input
      if (this.uploader) {
        this.uploader.nativeElement.value = '';
      }
      
      // Simulate file processing
      setTimeout(() => {
        this.isLoading = false;
        this.messageService.add({
          severity: 'success',
          summary: 'File Ready',
          detail: `${file.name} is ready for upload`,
          life: 3000
        });
      }, 1000);
    }
  }

  uploadFiles(files: File[]): void {
    const formData = new FormData();
    files.forEach(file => {
      formData.append('file', file, file.name);
    });
    
    formData.append('type', 'icici');
    this.showProgressBar = true;
    this.progressValue = 0;
    this.progressMessage = 'Uploading files...';
  
    this.http.post<number>('http://localhost:8090/bank/statement/upload', formData, {
      observe: 'response'
    }).subscribe({
      next: (response) => {
        const uniqueEmitterId = response.body;
        if (uniqueEmitterId !== null) {
          console.log('Files uploaded successfully! Emitter ID:', uniqueEmitterId);
          this.listenToSse(uniqueEmitterId);
        } else {
          throw new Error('No emitter ID received from server');
        }
      },
      error: (error) => {
        console.error('File upload failed!', error);
        this.showProgressBar = false;
        this.progressMessage = 'Upload failed';
        this.messageService.add({
          severity: 'error',
          summary: 'Upload Failed',
          detail: 'Failed to upload files. Please try again.'
        });
      }
    });
  }

  private eventSource: EventSource | null = null;

  listenToSse(uniqueEmitterId: number): void {
    this.eventSource = new EventSource(`http://localhost:8090/sse/emitter/${uniqueEmitterId}`);

    this.eventSource.onmessage = (event) => {
      this.zone.run(() => {
        const data: SSEData = JSON.parse(event.data);
        if (data.status === 'COMPLETED') {
          this.progressValue = 100;
          this.progressMessage = data.message || 'Processing complete!';
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: 'File processed successfully'
          });
          this.showProgressBar = false;
          this.loadTransactions(); // Refresh data
          this.eventSource?.close();
        } else if (data.status === 'IN_PROGRESS') {
          if (data.progress !== undefined) {
            this.progressValue = data.progress;
          }
          this.progressMessage = data.message || 'Processing...';
        } else if (data.status === 'ERROR') {
          this.progressMessage = 'Error: ' + (data.message || 'Unknown error occurred');
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to process file: ' + (data.message || 'Unknown error')
          });
          this.showProgressBar = false;
          this.eventSource?.close();
        }
      });
    };

    this.eventSource.onerror = () => {
      this.zone.run(() => {
        this.progressMessage = 'Connection error. Please try again.';
        this.showProgressBar = false;
        this.eventSource?.close();
      });
    };
  }
}
