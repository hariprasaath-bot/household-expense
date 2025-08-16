import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { InputNumberModule } from 'primeng/inputnumber';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { RippleModule } from 'primeng/ripple';
import { MultiEntryDialogComponent, CategoryEntry } from '../multi-entry-dialog/multi-entry-dialog.component';
import { CalculateInputDirective } from '../../../shared/directives/calculate-input.directive';
import { FinancialDataService, FinancialSummary } from '../../services/financial-data.service';

interface FinancialFormData {
  bankBalance: number;
  kiteWallet: number;
  dcxWallet: number;
  kitePosition: number;
  kiteHolding: number;
  dcxPosition: number;
  rupayCardBill: number;
  amazonCardBill: number;
  expense: number;
  earning: number;
  loss: number;
  earningEntries?: CategoryEntry[];
  lossEntries?: CategoryEntry[];
  timestamp?: Date;
}

@Component({
  selector: 'app-transaction-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonModule,
    InputNumberModule,
    ToastModule,
    RippleModule,
    MultiEntryDialogComponent,
    CalculateInputDirective
  ],
  providers: [MessageService],
  templateUrl: './transaction-dashboard.component.html',
  styleUrl: './transaction-dashboard.component.scss'
})
export class TransactionDashboardComponent implements OnInit {
  currentSummaryId: string | undefined = undefined;
  financialForm!: FormGroup;
  loading = false;
  
  // Multi-entry dialog properties
  showEarningDialog = false;
  showLossDialog = false;
  earningEntries: CategoryEntry[] = [];
  lossEntries: CategoryEntry[] = [];

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private financialDataService: FinancialDataService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.financialForm = this.fb.group({
      bankBalance: [0, Validators.required],
      kiteWallet: [0, Validators.required],
      dcxWallet: [0, Validators.required],
      kitePosition: [0, Validators.required],
      kiteHolding: [0, Validators.required],
      dcxPosition: [0, Validators.required],
      rupayCardBill: [0, Validators.required],
      amazonCardBill: [0, Validators.required],
      expense: [0, Validators.required],
      earning: [0, Validators.required],
      loss: [0, Validators.required]
    });

    // No need to call updateTotals() here, template handles it.

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.currentSummaryId = id;
        this.loadSummaryForEdit(id);
      }
    });
  }

  calculateTotalAssets(): number {
    const form = this.financialForm.value;
    const bankBalance = parseFloat(form.bankBalance) || 0;
    const kiteWallet = parseFloat(form.kiteWallet) || 0;
    const dcxWallet = parseFloat(form.dcxWallet) || 0;
    const kitePosition = parseFloat(form.kitePosition) || 0;
    const kiteHolding = parseFloat(form.kiteHolding) || 0;
    const dcxPosition = parseFloat(form.dcxPosition) || 0;

    return (
      bankBalance +
      kiteWallet +
      dcxWallet +
      (kitePosition > 0 ? kitePosition : 0) +
      kiteHolding +
      (dcxPosition > 0 ? dcxPosition : 0)
    );
  }

  calculateTotalLiabilities(): number {
    const form = this.financialForm.value;
    const rupayCardBill = parseFloat(form.rupayCardBill) || 0;
    const amazonCardBill = parseFloat(form.amazonCardBill) || 0;
    const expense = parseFloat(form.expense) || 0;
    const loss = parseFloat(form.loss) || 0;
    const kitePosition = parseFloat(form.kitePosition) || 0;
    const dcxPosition = parseFloat(form.dcxPosition) || 0;

    return (
      rupayCardBill +
      amazonCardBill +
      expense +
      loss +
      (kitePosition < 0 ? Math.abs(kitePosition) : 0) +
      (dcxPosition < 0 ? Math.abs(dcxPosition) : 0)
    );
  }

  // Methods for handling earning entries
  openEarningDialog() {
    // Pass a copy of the array to avoid direct mutation
    this.showEarningDialog = true;
  }

  onEarningEntriesSaved(entries: CategoryEntry[]) {
    this.earningEntries = entries;
    const totalEarning = entries.reduce((sum, entry) => sum + entry.value, 0);
    this.financialForm.patchValue({ earning: totalEarning });
    this.showEarningDialog = false;
    
    this.messageService.add({
      severity: 'success',
      summary: 'Earning Entries Added',
      detail: `${entries.length} entries totaling ₹${totalEarning.toFixed(2)}`
    });
  }

  onEarningDialogCancelled() {
    this.showEarningDialog = false;
  }

  addSingleEarning() {
    const currentEarning = this.financialForm.get('earning')?.value || 0;
    if (currentEarning > 0) {
      this.earningEntries = [{ category: 'unknown', value: currentEarning }];
      this.messageService.add({
        severity: 'info',
        summary: 'Single Entry Added',
        detail: `Earning: ₹${currentEarning} (Category: unknown)`
      });
    }
  }

  // Methods for handling loss entries
  openLossDialog() {
    // Pass a copy of the array to avoid direct mutation
    this.showLossDialog = true;
  }

  onLossEntriesSaved(entries: CategoryEntry[]) {
    this.lossEntries = entries;
    const totalLoss = entries.reduce((sum, entry) => sum + entry.value, 0);
    this.financialForm.patchValue({ loss: totalLoss });
    this.showLossDialog = false;
    
    this.messageService.add({
      severity: 'success',
      summary: 'Loss Entries Added',
      detail: `${entries.length} entries totaling ₹${totalLoss.toFixed(2)}`
    });
  }

  onLossDialogCancelled() {
    this.showLossDialog = false;
  }

  addSingleLoss() {
    const currentLoss = this.financialForm.get('loss')?.value || 0;
    if (currentLoss > 0) {
      this.lossEntries = [{ category: 'unknown', value: currentLoss }];
      this.messageService.add({
        severity: 'info',
        summary: 'Single Entry Added',
        detail: `Loss: ₹${currentLoss} (Category: unknown)`
      });
    }
  }

  // Get entry details for display
  getEarningEntriesText(): string {
    if (this.earningEntries.length === 0) return '';
    if (this.earningEntries.length === 1 && this.earningEntries[0].category === 'unknown') {
      return 'Single entry (unknown category)';
    }
    return `${this.earningEntries.length} categories`;
  }

  getLossEntriesText(): string {
    if (this.lossEntries.length === 0) return '';
    if (this.lossEntries.length === 1 && this.lossEntries[0].category === 'unknown') {
      return 'Single entry (unknown category)';
    }
    return `${this.lossEntries.length} categories`;
  }

  onSubmit() {
    if (this.financialForm.invalid) {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail: 'Please check all fields and try again.'
      });
      return;
    }

    this.loading = true;
    const formValues = this.financialForm.value;

    const summaryPayload: FinancialSummary = {
      bankBalance: String(formValues.bankBalance),
      kiteWallet: String(formValues.kiteWallet),
      dcxWallet: parseFloat(formValues.dcxWallet),
      kitePosition: String(formValues.kitePosition),
      kiteHolding: String(formValues.kiteHolding),
      dcxPosition: String(formValues.dcxPosition),
      rupayCardBill: String(formValues.rupayCardBill),
      amazonCardBill: String(formValues.amazonCardBill),
      expense: parseFloat(formValues.expense),
      earning: String(formValues.earning),
      loss: parseFloat(formValues.loss),
      lossEntries: this.lossEntries.length > 0 ? this.lossEntries : undefined,
      timestamp: new Date().toISOString(),
    };

    const saveObservable = this.currentSummaryId
      ? this.financialDataService.updateFinancialSummary(this.currentSummaryId, summaryPayload)
      : this.financialDataService.saveFinancialSummary(summaryPayload);

    saveObservable.subscribe({
      next: (response) => {
        this.loading = false;
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: `Financial data ${this.currentSummaryId ? 'updated' : 'saved'} successfully!`,
        });
        this.router.navigate(['/transactions/summaries']); // Navigate back to the list
      },
      error: (error) => {
        this.loading = false;
        this.messageService.add({
          severity: 'error',
          summary: 'API Error',
          detail: 'Failed to save data. Please try again.',
        });
        console.error('API Error:', error);
      },
    });
  }

  onReset() {
    this.financialForm.reset({
      bankBalance: 0,
      kiteWallet: 0,
      dcxWallet: 0,
      kitePosition: 0,
      kiteHolding: 0,
      dcxPosition: 0,
      rupayCardBill: 0,
      amazonCardBill: 0,
      expense: 0,
      earning: 0,
      loss: 0
    });
    
    // Reset entry arrays
    this.earningEntries = [];
    this.lossEntries = [];
    
    this.messageService.add({
      severity: 'info',
      summary: 'Reset',
      detail: 'Form has been reset'
    });
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.financialForm.get(fieldName);
    return field ? field.invalid && (field.dirty || field.touched) : false;
  }

  private loadSummaryForEdit(id: string): void {
    this.financialDataService.getFinancialSummaryById(id).subscribe({
      next: (summary: FinancialSummary) => {
        this.financialForm.patchValue({
          bankBalance: summary.bankBalance,
          kiteWallet: summary.kiteWallet,
          dcxWallet: summary.dcxWallet,
          kitePosition: summary.kitePosition,
          kiteHolding: summary.kiteHolding,
          dcxPosition: summary.dcxPosition,
          rupayCardBill: summary.rupayCardBill,
          amazonCardBill: summary.amazonCardBill,
          expense: summary.expense,
          earning: summary.earning,
          loss: summary.loss
        });
        this.lossEntries = summary.lossEntries || [];
      },
      error: (err) => {
        this.messageService.add({ 
          severity: 'error', 
          summary: 'Error',
          detail: 'Failed to load financial data for editing.'
        });
        this.router.navigate(['/transactions/summaries']);
      }
    });
  }
}
