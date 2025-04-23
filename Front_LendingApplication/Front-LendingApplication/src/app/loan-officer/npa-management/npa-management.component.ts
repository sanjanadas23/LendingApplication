import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { LoanOfficerService } from '../services/loan-officer.service';
import { AuthService } from '../../core/auth/auth.service';
import { LoanResponseDTO } from '../models/loan-officer.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-npa-management',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './npa-management.component.html',
  styleUrls: ['./npa-management.component.css']
})
export class NpaManagementComponent implements OnInit {
  loans: LoanResponseDTO[] = [];
  errorMessage: string | null = null;
  displayedColumns: string[] = ['loanId', 'customerId', 'amount', 'statusName', 'actions'];

  constructor(
    private loanOfficerService: LoanOfficerService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const loanOfficerId = this.authService.getLoanOfficerId();
    if (loanOfficerId) {
      this.loadNpaLoans(loanOfficerId);
    }
  }

  loadNpaLoans(loanOfficerId: number): void {
    this.loanOfficerService.getNpaLoans(loanOfficerId).subscribe({
      next: loans => {
        this.loans = loans;
        if (loans.length === 0) {
          this.errorMessage = 'No NPA loans found.';
        }
      },
      error: err => {
        this.errorMessage = 'Failed to load NPA loans.';
      }
    });
  }

  markAsNPA(loan: LoanResponseDTO): void {
    this.loanOfficerService.markAsNPA(loan.loanId, { approve: true }).subscribe({
      next: updatedLoan => {
        this.loans = this.loans.map(l => l.loanId === updatedLoan.loanId ? updatedLoan : l);
        this.errorMessage = null;
      },
      error: (err: Error) => {
        this.errorMessage = `Failed to mark loan as NPA: ${err.message}`;
      }
    });
  }

  viewDocuments(loanId: number): void {
    this.router.navigate([`/loan-officer/documents/${loanId}`]);
  }
}