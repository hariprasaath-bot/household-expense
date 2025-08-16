import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormArray } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { MessageService } from 'primeng/api';
import { CalculateInputDirective } from '../../../shared/directives/calculate-input.directive';

export interface CategoryEntry {
  category: string;
  value: number;
}

@Component({
  selector: 'app-multi-entry-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    DialogModule,
    ButtonModule,
    InputNumberModule,
    InputTextModule,
    TableModule,
    CalculateInputDirective
  ],
  templateUrl: './multi-entry-dialog.component.html',
  styleUrls: ['./multi-entry-dialog.component.scss']
})
export class MultiEntryDialogComponent implements OnChanges {
  @Input() visible = false;
  @Input() dialogTitle = 'Add Multiple Entries';
  @Input() initialEntries: CategoryEntry[] = [];
  @Output() visibleChange = new EventEmitter<boolean>();
  @Output() entriesSaved = new EventEmitter<CategoryEntry[]>();
  @Output() cancelled = new EventEmitter<void>();

  dialogForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService
  ) {
    this.initializeForm();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['visible'] && this.visible) {
      this.populateFormWithInitialEntries();
    }
  }

  private initializeForm() {
    this.dialogForm = this.fb.group({
      entries: this.fb.array([])
    });
  }

  private populateFormWithInitialEntries() {
    this.entries.clear();
    if (this.initialEntries && this.initialEntries.length > 0) {
      this.initialEntries.forEach(entry => {
        this.entries.push(this.createEntryGroup(entry));
      });
    } else {
      // Start with one empty entry if none are provided
      this.addEntry();
    }
  }

  get entries(): FormArray {
    return this.dialogForm.get('entries') as FormArray;
  }

  createEntryGroup(entry: CategoryEntry = { category: '', value: 0 }): FormGroup {
    return this.fb.group({
      category: [entry.category, [Validators.required, Validators.minLength(2)]],
      value: [entry.value, [Validators.required, Validators.min(0.01)]]
    });
  }

  addEntry() {
    this.entries.push(this.createEntryGroup());
  }

  removeEntry(index: number) {
    this.entries.removeAt(index);
    this.messageService.add({
      severity: 'info',
      summary: 'Entry Removed',
      detail: 'Entry has been removed from the list.'
    });

    if (this.entries.length === 0) {
      this.addEntry(); // Ensure there's always one row to enter data
    }
  }

  getCumulativeTotal(): number {
    return this.entries.value.reduce((total: number, entry: CategoryEntry) => total + (entry.value || 0), 0);
  }

  onSave() {
    if (this.dialogForm.valid && this.entries.length > 0) {
      this.entriesSaved.emit(this.dialogForm.value.entries);
      this.closeDialog();
    } else {
      this.messageService.add({ 
        severity: 'error', 
        summary: 'Invalid Entries', 
        detail: 'Please ensure all categories and values are filled correctly.' 
      });
    }
  }

  onCancel() {
    this.cancelled.emit();
    this.closeDialog();
  }

  private closeDialog() {
    this.visible = false;
    this.visibleChange.emit(false);
  }
}
