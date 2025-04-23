import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CustomerService } from '../services/customer.service';
import { AuthService } from '../../core/auth/auth.service';
import { Loan, LoanPayment, Profile } from '../models/customer.model';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-make-payment',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    ReactiveFormsModule
  ],
  templateUrl: './make-payment.component.html',
  styleUrls: ['./make-payment.component.css']
})
export class MakePaymentComponent implements OnInit {
  customerId: number | null;
  loans: Loan[] = [];
  selectedLoan: Loan | null = null;
  payments: LoanPayment[] = [];
  profile: Profile | null = null;
  error: string | null = null;
  loading = false;
  paymentForm: FormGroup;
  displayedColumns: string[] = ['id', 'amount', 'dueDate', 'status', 'penaltyAmount', 'action'];

  constructor(
    private customerService: CustomerService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.customerId = this.authService.getCustomerId();
    this.paymentForm = this.fb.group({
      loanId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    if (!this.customerId) {
      this.error = 'Please log in to make a payment.';
      this.router.navigate(['/login']);
      return;
    }

    this.customerService.getProfile(this.customerId).subscribe({
      next: (profile: Profile) => this.profile = profile,
      error: (err: Error) => {
        this.error = `Failed to load profile: ${err.message}`;
      }
    });

    this.customerService.getCustomerLoans(this.customerId).subscribe({
      next: loans => {
        this.loans = loans;
        const loanId = this.route.snapshot.paramMap.get('loanId');
        if (loanId) {
          this.selectedLoan = loans.find(loan => loan.loanId === +loanId) || null;
          if (this.selectedLoan) {
            this.paymentForm.patchValue({ loanId: this.selectedLoan.loanId });
            this.loadPayments(this.selectedLoan.loanId);
          }
        }
      },
      error: err => {
        this.error = `Failed to load loans: ${err.message}`;
      }
    });
  }

  selectLoan(loanId: number): void {
    this.selectedLoan = this.loans.find(loan => loan.loanId === loanId) || null;
    if (this.selectedLoan) {
      this.loadPayments(this.selectedLoan.loanId);
    }
  }

  loadPayments(loanId: number): void {
    this.loading = true;
    this.customerService.getLoanPayments(loanId, 'PENDING').subscribe({
      next: payments => {
        this.payments = payments;
        this.loading = false;
      },
      error: err => {
        this.error = `Failed to load payments: ${err.message}`;
        this.loading = false;
      }
    });
  }

  makePayment(paymentId: number): void {
    if (!this.customerId) return;
    this.loading = true;

    this.customerService.initiatePayment(paymentId).subscribe({
      next: orderId => {
        this.loading = false;
        // Simulate Razorpay payment
        const options = {
          key: 'rzp_test_key',
          amount: 0,
          currency: 'INR',
          name: 'LendEase',
          description: 'Loan Payment',
          order_id: orderId,
          handler: (response: any) => {
            this.completePayment(response, paymentId);
          },
          prefill: {
            name: this.profile?.firstName + ' ' + this.profile?.lastName,
            email: this.profile?.email,
            contact: this.profile?.mobileNumber
          }
        };

        this.customerService.getPaymentAmount(paymentId).subscribe({
          next: amount => {
            options.amount = parseFloat(amount) * 100;
            const rzp = new (window as any).Razorpay(options);
            rzp.open();
          },
          error: err => {
            this.error = `Failed to fetch payment amount: ${err.message}`;
            this.loading = false;
          }
        });
      },
      error: err => {
        this.error = `Failed to initiate payment: ${err.message}`;
        this.loading = false;
      }
    });
  }

  completePayment(response: any, paymentId: number): void {
    const paymentData = {
      razorpay_payment_id: response.razorpay_payment_id,
      razorpay_order_id: response.razorpay_order_id,
      razorpay_signature: response.razorpay_signature,
      paymentId: paymentId
    };

    this.customerService.completePayment(paymentData).subscribe({
      next: () => {
        this.error = null;
        if (this.selectedLoan) {
          this.loadPayments(this.selectedLoan.loanId);
        }
      },
      error: err => {
        this.error = `Failed to complete payment: ${err.message}`;
      }
    });
  }
}