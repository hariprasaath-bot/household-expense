import { Routes } from '@angular/router';
import { UserManagementComponent } from './user-management/root/user-management/user-management.component';
import { AuthGuard } from './auth/auth-gaurd.guard';
import { transactionRoutes } from './transactions/transactions.route';
import { TransactionsRootComponent } from './transactions/transactions-root/transactions-root.component';
import { AdminRootComponent } from './admin-activity/admin-root/admin-root.component';
import { adminsRoutes } from './admin-activity/admins.route';
import { UserProfileComponent } from './user-management/components/user-profile/user-profile.component';

export const routes: Routes = [
    { path: '', redirectTo: 'profile', pathMatch: 'full'},
    { path: 'sign-up', component: UserManagementComponent },
    { path: 'profile', component: UserProfileComponent, canActivate: [AuthGuard] },
    { 
        path: 'transactions', 
        component: TransactionsRootComponent, // Parent component
        canActivate: [AuthGuard], // Apply AuthGuard
        children: transactionRoutes // Child routes
    },
    { 
        path: 'admin', 
        component: AdminRootComponent, // Parent component
        canActivate: [AuthGuard], // Apply AuthGuard                    
        children: adminsRoutes // Child routes
    },
];