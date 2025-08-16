import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

export interface AuthState {
  accessToken: string | null;
  refreshToken: string | null;
  userId: string | null;
  email: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8090/auth';
  private refreshTokenTimeout?: number;

  // Using signals for reactive state management
  private authState = signal<AuthState>({
    accessToken: null,
    refreshToken: null,
    userId: null,
    email: null
  });

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.loadStoredAuth();
  }

  private loadStoredAuth() {
    const storedAuth = localStorage.getItem('auth');
    if (storedAuth) {
      this.authState.set(JSON.parse(storedAuth));
      this.startRefreshTokenTimer();
    }
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post<AuthState>(`${this.API_URL}/signin`, { email, password })
      .pipe(
        tap(response => {
          this.authState.set(response);
          localStorage.setItem('auth', JSON.stringify(response));
          this.startRefreshTokenTimer();
  
          // Redirect to the intended route or default route after login
          const returnUrl = this.router.parseUrl(this.router.url).queryParams['returnUrl'] || '/transactions';
          this.router.navigateByUrl('/transactions');
        }),
        catchError(error => throwError(() => error))
      );
  }

  logout() {
    localStorage.removeItem('auth');
    this.authState.set({
      accessToken: null,
      refreshToken: null,
      userId: null,
      email: null
    });
    this.stopRefreshTokenTimer();
    this.router.navigate(['/sign-up']);
  }

  refreshToken(): Observable<any> {
    const refreshToken = this.authState().refreshToken;
    if (!refreshToken) {
      return throwError(() => new Error('No refresh token available'));
    }

    return this.http.post<any>(`${this.API_URL}/refresh`, { refreshToken })
      .pipe(
        tap(response => {
          const newState = {
            ...this.authState(),
            accessToken: response.accessToken
          };
          this.authState.set(newState);
          localStorage.setItem('auth', JSON.stringify(newState));
          this.startRefreshTokenTimer();
        }),
        catchError(error => throwError(() => error))
      );
  }

  private startRefreshTokenTimer() {
    const token = this.authState().accessToken;
    if (!token) return;

    try {
      const decoded = jwtDecode(token);
      const expires = decoded.exp! * 1000; // Convert to milliseconds
      const timeout = expires - Date.now() - (60 * 1000); // Refresh 1 minute before expiry
      
      this.refreshTokenTimeout = window.setTimeout(() => {
        this.refreshToken().subscribe();
      }, timeout);
    } catch (e) {
      console.error('Error decoding token:', e);
    }
  }

  private stopRefreshTokenTimer() {
    if (this.refreshTokenTimeout) {
      window.clearTimeout(this.refreshTokenTimeout);
    }
  }

  isAuthenticated(): boolean {
    const token = this.authState().accessToken;
    if (!token) return false;

    try {
      const decoded = jwtDecode(token);
      return decoded.exp! * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  getAccessToken(): string | null {
    return this.authState().accessToken;
  }
}
