import { Routes } from '@angular/router';
import { RolesComponent } from './components/roles/roles.component';
import { AuthGuard } from '../auth/auth-gaurd.guard';
import { StatsComponent } from './components/stats/stats.component';

export const adminsRoutes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'stats'
    },
    {
        path: 'stats',
        component: StatsComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'roles',
        component: RolesComponent,
        canActivate: [AuthGuard]
    }

];