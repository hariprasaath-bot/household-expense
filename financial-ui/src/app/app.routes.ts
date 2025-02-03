import { Routes } from '@angular/router';
import { UserManagementComponent } from './user-management/root/user-management/user-management.component';
import { LandingComponent } from './home-component/components/landing/landing.component';
import { AuthGuard } from './auth/auth-gaurd.guard';
import { transactionRoutes } from './transactions/transactions.route';
import { TransactionsRootComponent } from './transactions/transactions-root/transactions-root.component';

export const routes: Routes = [
    { path: '', redirectTo: 'transactions', pathMatch: 'full' },
    { path: 'sign-up', component: UserManagementComponent },
    { path: 'home', component: LandingComponent, canActivate : [AuthGuard] },
    { 
        path: 'transactions', 
        component: TransactionsRootComponent, // Parent component
        canActivate: [AuthGuard], // Apply AuthGuard
        children: transactionRoutes // Child routes
    },
];