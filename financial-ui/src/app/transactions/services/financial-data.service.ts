import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Based on the Spring Boot @Data class
export interface FinancialSummary {
  id?: string; // Unique identifier from the backend
  bankBalance: string;
  kiteWallet: string;
  dcxWallet: number;
  kitePosition: string;
  kiteHolding: string;
  dcxPosition: string;
  rupayCardBill: string;
  amazonCardBill: string;
  expense: number;
  earning: string;
  loss: number;
  lossEntries?: { category: string; value: number }[];
  timestamp: string; // ISO 8601 format
  userId?: string;
}

@Injectable({   
  providedIn: 'root'
})
export class FinancialDataService {
private baseUrl = 'http://localhost:8090/'
private apiUrl = 'finance/summary'; // Using proxy, so relative path is fine

  constructor(private http: HttpClient) { }

  saveFinancialSummary(summary: FinancialSummary): Observable<FinancialSummary> {
    return this.http.post<FinancialSummary>(this.baseUrl + this.apiUrl, summary);
  }

  getFinancialSummaries(): Observable<FinancialSummary[]> {
    return this.http.get<FinancialSummary[]>(this.baseUrl + this.apiUrl); // Assuming endpoint for list is /summaries
  }

  getFinancialSummaryById(id: string): Observable<FinancialSummary> {
    return this.http.get<FinancialSummary>(`${this.baseUrl}${this.apiUrl}/${id}`);
  }

  updateFinancialSummary(id: string, summary: FinancialSummary): Observable<FinancialSummary> {
    return this.http.put<FinancialSummary>(`${this.baseUrl}${this.apiUrl}/${id}`, summary);
  }
}
