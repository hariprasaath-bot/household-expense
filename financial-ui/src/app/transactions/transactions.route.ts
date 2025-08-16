import { Routes } from '@angular/router';
import { TransactionUploadComponent } from './components/transaction-upload/transaction-upload.component';
import { TransactionDashboardComponent } from './components/transaction-dashboard/transaction-dashboard.component';
import { SummaryListComponent } from './components/summary-list/summary-list.component';
import { SupersetEmbedComponent } from './components/superset-embed/superset-embed.component';


export const transactionRoutes: Routes = [
  { path: '', redirectTo: 'summaries', pathMatch: 'full' },
  { path: 'summaries', component: SummaryListComponent },
  { path: 'dashboard', component: TransactionDashboardComponent }, // For creating new entries
  { path: 'dashboard/:id', component: TransactionDashboardComponent }, // For editing existing entries
  { path: 'manipulate', component: TransactionUploadComponent },
  { path: 'superset', component: SupersetEmbedComponent }
];