import { Routes } from '@angular/router';
import { TransactionUploadComponent } from './components/transaction-upload/transaction-upload.component';
import { TransactionDashboardComponent } from './components/transaction-dashboard/transaction-dashboard.component';


export const transactionRoutes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'manipulate', component: TransactionUploadComponent },
  { path: 'dashboard', component: TransactionDashboardComponent }
];