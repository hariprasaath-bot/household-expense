import { Routes } from '@angular/router';
import { UserManagementComponent } from './user-management/root/user-management/user-management.component';
import { LandingComponent } from './home-component/components/landing/landing.component';

export const routes: Routes = [
    { path: '', redirectTo: 'sign-up', pathMatch: 'full' },
    { path: 'sign-up', component: UserManagementComponent },
    { path: 'home', component: LandingComponent },
];