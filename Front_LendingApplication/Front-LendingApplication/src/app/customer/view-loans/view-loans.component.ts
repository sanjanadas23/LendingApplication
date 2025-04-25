// import { Component, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { MatCardModule } from '@angular/material/card';
// import { MatTableModule } from '@angular/material/table';
// import { MatButtonModule } from '@angular/material/button';
// import { CustomerService } from '../services/customer.service';
// import { AuthService } from '../../core/auth/auth.service';
// import { Loan, LoanScheme } from '../models/customer.model';
// import { Router } from '@angular/router';

// @Component({
//   selector: 'app-view-loans',
//   standalone: true,
//   imports: [
//     CommonModule,
//     MatCardModule,
//     MatTableModule,
//     MatButtonModule
//   ],
//   templateUrl: './view-loans.component.html',
//   styleUrls: ['./view-loans.component.css']
// })
// export class ViewLoansComponent implements OnInit {
//   loans: Loan[] = [];
//   loanSchemes: LoanScheme[] = [];
//   errorMessage: string | null = null;
//   customerId: number | null;
//   displayedColumns: string[] = ['loanId', 'schemeName', 'amount', 'status', 'actions'];

//   constructor(
//     private customerService: CustomerService,
//     private authService: AuthService,
//     private router: Router
//   ) {
//     this.customerId = this.authService.getCustomerId();
//   }

//   ngOnInit(): void {
//     if (!this.customerId) {
//       this.errorMessage = 'Please log in to view loans.';
//       this.router.navigate(['/login']);
//       return;
//     }

//     this.loadLoanSchemes();
//     this.loadLoans();
//   }

//   loadLoanSchemes(): void {
//     this.customerService.getLoanSchemes().subscribe({
//       next: (schemes) => this.loanSchemes = schemes.filter(s => !s.isDeleted),
//       error: (err: Error) => {
//         this.errorMessage = `Failed to load loan schemes: ${err.message}`;
//       }
//     });
//   }

//   loadLoans(): void {
//     if (this.customerId) {
//       this.customerService.getCustomerLoans(this.customerId).subscribe({
//         next: (loans) => {
//           this.loans = loans;
//           if (loans.length === 0) {
//             this.errorMessage = 'No loans found.';
//           }
//         },
//         error: (err: Error) => {
//           this.errorMessage = `Failed to load loans: ${err.message}`;
//         }
//       });
//     }
//   }

//   viewDetails(loanId: number): void {
//     this.router.navigate([`/customer/loans/${loanId}`]);
//   }
// }

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { CustomerService } from '../services/customer.service';
import { AuthService } from '../../core/auth/auth.service';
import { Loan, LoanScheme } from '../models/customer.model';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-view-loans',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    RouterModule
  ],
  templateUrl: './view-loans.component.html',
  styleUrls: ['./view-loans.component.css']
})
export class ViewLoansComponent implements OnInit {
  loans: Loan[] = [];
  loanSchemes: LoanScheme[] = [];
  errorMessage: string | null = null;
  customerId: number | null;

  displayedLoanColumns: string[] = ['loanId', 'loanSchemeName', 'amount', 'statusName', 'applicationDate', 'dueDate'];
  displayedSchemeColumns: string[] = ['schemeName', 'interestRate', 'tenureMonths', 'action'];

  constructor(
    private customerService: CustomerService,
    private authService: AuthService,
    private router: Router
  ) {
    this.customerId = this.authService.getCustomerId();
  }

  ngOnInit(): void {
    if (!this.customerId) {
      this.errorMessage = 'Please log in to view loans.';
      this.router.navigate(['/login']);
      return;
    }

    this.loadLoanSchemes();
    this.loadLoans();
  }

  loadLoanSchemes(): void {
    this.customerService.getLoanSchemes().subscribe({
      next: (schemes) => {
        console.log('Loan schemes loaded:', schemes);
        this.loanSchemes = schemes.filter(s => !s.isDeleted);
      },
      error: (err: Error) => {
        this.errorMessage = `Failed to load loan schemes: ${err.message}`;
      }
    });
  }

  loadLoans(): void {
    if (this.customerId) {
      this.customerService.getCustomerLoans(this.customerId).subscribe({
        next: (loans) => {
          console.log('Loans loaded:', loans);
          this.loans = loans;
          if (loans.length === 0) {
            this.errorMessage = 'No loans found.';
          }
        },
        error: (err: Error) => {
          this.errorMessage = `Failed to load loans: ${err.message}`;
        }
      });
    }
  }

  viewDetails(loanId: number): void {
    this.router.navigate([`/customer/loans/${loanId}`]);
  }
}
