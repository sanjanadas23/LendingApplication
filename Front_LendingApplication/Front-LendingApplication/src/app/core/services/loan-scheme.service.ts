import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  LoanSchemeRequest,
  LoanSchemeResponse,
  LoanSchemeUpdate,
  LoanSchemeSoftDelete
} from '../../admin/models/loan-scheme.model';
import { map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class LoanSchemeService {
  private apiUrl = `${environment.apiUrl}/api/loan-schemes`;
  private adminApiUrl = `${environment.apiUrl}/api/admin/loan-schemes`;

  constructor(private http: HttpClient) { }

  createLoanScheme(adminId: number, request: LoanSchemeRequest): Observable<LoanSchemeResponse> {
    const url = `${this.apiUrl}/create?adminId=${adminId}`;
    console.log('Adding loan scheme:', request, 'to:', url);
    return this.http.post<LoanSchemeResponse>(url, request).pipe(
      tap(response => console.log('Loan scheme added:', response)),
      tap({ error: err => console.error('Add loan scheme failed:', err) })
    );
  }

  updateLoanScheme(id: number, adminId: number, request: LoanSchemeUpdate): Observable<LoanSchemeResponse> {
    const url = `${this.apiUrl}/${id}/update?adminId=${adminId}`;
    console.log('Updating loan scheme:', id, request, 'to:', url);
    return this.http.put<LoanSchemeResponse>(url, request).pipe(
      tap(response => console.log('Loan scheme updated:', response)),
      tap({ error: err => console.error('Update loan scheme failed:', err) })
    );
  }

  softDeleteLoanScheme(id: number, request: LoanSchemeSoftDelete): Observable<void> {
    const url = `${this.adminApiUrl}/${id}/soft-delete`;
    console.log('Soft deleting loan scheme:', id, 'to:', url);
    return this.http.put<void>(url, request).pipe(
      tap(() => console.log('Loan scheme soft deleted:', id)),
      tap({ error: err => console.error('Soft delete loan scheme failed:', err) })
    );
  }

  getAllLoanSchemes(): Observable<LoanSchemeResponse[]> {
    console.log('Fetching loan schemes from:', this.adminApiUrl);
    return this.http.get<any[]>(this.adminApiUrl).pipe(
      map(schemes => schemes.map(scheme => ({
        ...scheme,
        isDeleted: scheme.deleted // Map backend 'deleted' to frontend 'isDeleted'
      }))),
      tap(data => console.log('Mapped loan schemes:', data)),
      tap({ error: err => console.error('Fetch loan schemes failed:', err) })
    );
  }
}