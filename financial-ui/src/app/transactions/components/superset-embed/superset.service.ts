import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SupersetService {
  private apiUrl = 'http://localhost:8088/api/v1/security';

  constructor(private http: HttpClient) { }

  private fetchAccessToken(): Observable<any> {
    const body = {
      "username": "admin",
      "password": "admin",
      "provider": "db",
      "refresh": true
    };
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.post<any>(`${this.apiUrl}/login`, body, { headers });
  }

  private fetchGuestToken(accessToken: any): Observable<any> {
    const body = {
      "resources": [
        {
          "type": "dashboard",
          "id": "4d2c303c-b2fb-42df-9375-30834c9e62ee",
        }
      ],
      "rls": [{ "clause": "stage_of_development = 'Pre-clinical'" }],
      "user": {
        "username": "guest",
        "first_name": "Guest",
        "last_name": "User",
      }
    };
    const acc = accessToken["access_token"];
    const headers = new HttpHeaders({
      "Content-Type": "application/json",
      "Authorization": `Bearer ${acc}`,
    });
    return this.http.post<any>(`${this.apiUrl}/guest_token/`, body, { headers });
  }

  getGuestToken(): Observable<any> {
    return this.fetchAccessToken().pipe(
      catchError((error) => {
        console.error(error);
        return throwError(error);
      }),
      switchMap((accessToken: any) => this.fetchGuestToken(accessToken))
    );
  }
}