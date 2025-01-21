import { Routes } from '@angular/router';
import { UserManagementComponent } from './user-management/root/user-management/user-management.component';
import { LandingComponent } from './home-component/components/landing/landing.component';
import { TransactionsRootComponent } from './transactions/transactions-root/transactions-root.component';
import { AuthGuard } from './auth/auth-gaurd.guard';

export const routes: Routes = [
    { path: '', redirectTo: 'transactions', pathMatch: 'full' },
    { path: 'sign-up', component: UserManagementComponent },
    { path: 'home', component: LandingComponent, canActivate : [AuthGuard] },
    { path: 'transactions', component: TransactionsRootComponent, canActivate : [AuthGuard] },
];