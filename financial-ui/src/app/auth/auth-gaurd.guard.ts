import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './service/auth.service';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authService: AuthService,
    private messageService: MessageService
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.authService.isAuthenticated()) {
      return true;
    }
    this.messageService.add({ severity: 'error', summary: 'Error', detail: 'You are not logged in', life: 1500 });
    // Not logged in so redirect to login page with the return url
    this.router.navigate(['/sign-up'], { queryParams: { returnUrl: state.url } });
    return false;
  }
}