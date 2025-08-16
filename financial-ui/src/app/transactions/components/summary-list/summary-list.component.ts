import { trigger, transition, style, animate, query, stagger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { FinancialDataService, FinancialSummary } from '../../services/financial-data.service';

@Component({
  selector: 'app-summary-list',
  standalone: true,
  imports: [CommonModule, CardModule, ButtonModule],
  templateUrl: './summary-list.component.html',
  styleUrls: ['./summary-list.component.scss'],
  animations: [
    trigger('listAnimation', [
      transition('* <=> *', [
        query(':enter',
          [style({ opacity: 0, transform: 'translateY(-15px)' }), stagger('50ms', animate('550ms ease-out', style({ opacity: 1, transform: 'translateY(0px)' })))],
          { optional: true }
        )
      ])
    ])
  ]
})
export class SummaryListComponent implements OnInit {
  summaries: FinancialSummary[] = [];
  loading = true;
  Math = Math;

  constructor(
    private financialDataService: FinancialDataService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.financialDataService.getFinancialSummaries().subscribe({
      next: (data) => {
        this.summaries = data.sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load summaries', err);
        this.loading = false;
        // Optionally, add user-facing error message
      }
    });
  }

  editSummary(id: string | undefined): void {
    if (id) {
      this.router.navigate(['/transactions/dashboard', id]);
    }
  }

  safeParseFloat(value: string | number): number {
    if (typeof value === 'number') {
      return value;
    }
    const parsed = parseFloat(value);
    return isNaN(parsed) ? 0 : parsed;
  }
}
