// import { Component, OnInit } from '@angular/core';
// import { CustomerService } from '../services/customer.service';
// import { AuthService } from '../../core/auth/auth.service';
// import { CommonModule } from '@angular/common';
// import { RouterModule, Router } from '@angular/router';
// import { MatCardModule } from '@angular/material/card';
// import { MatButtonModule } from '@angular/material/button';
// import { HeaderComponent } from '../../shared/components/header/header.component';

// @Component({
//   selector: 'app-customer-dashboard',
//   standalone: true,
//   imports: [
//     CommonModule,
//     RouterModule,
//     MatCardModule,
//     MatButtonModule,
//     HeaderComponent
//   ],
//   templateUrl: './customer-dashboard.component.html',
//   styleUrls: ['./customer-dashboard.component.css']
// })
// export class CustomerDashboardComponent implements OnInit {
//   customerId: number | null;
//   error: string | null = null;

//   constructor(
//     private customerService: CustomerService,
//     private authService: AuthService,
//     private router: Router
//   ) {
//     this.customerId = this.authService.getCustomerId();
//   }

//   ngOnInit(): void {
//     if (!this.customerId) {
//       this.error = 'Please log in to view your dashboard.';
//       this.router.navigate(['/login']);
//     }
//   }
// }


import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../services/customer.service';
import { AuthService } from '../../core/auth/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { HeaderComponent } from '../../shared/components/header/header.component';

@Component({
  selector: 'app-customer-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatButtonModule,
    HeaderComponent
  ],
  templateUrl: './customer-dashboard.component.html',
  styleUrls: ['./customer-dashboard.component.css']
})
export class CustomerDashboardComponent implements OnInit {
  customerId: number | null;
  error: string | null = null;

  cards = [
    {
      title: 'Apply for Loan',
      description: 'Choose from various loan schemes and apply easily.',
      buttonText: 'Apply Now',
      link: '/customer/apply-loan'
    },
    {
      title: 'View All Loans',
      description: 'Explore all available loan schemes.',
      buttonText: 'View Schemes',
      link: '/customer/view-loans'
    },
    {
      title: 'View My Loans',
      description: 'Check the status of your loan applications.',
      buttonText: 'View My Loans',
      link: '/customer/view-loans'
    },
    {
      title: 'Make Payment',
      description: 'Pay your loan installments securely.',
      buttonText: 'Make Payment',
      link: '/customer/make-payment'
    },
    {
      title: 'Payment Details',
      description: 'View your payment history and pending installments.',
      buttonText: 'View Payments',
      link: '/customer/payment-details'
    }
  ];

  constructor(
    private customerService: CustomerService,
    private authService: AuthService,
    private router: Router
  ) {
    this.customerId = this.authService.getCustomerId();
  }

  ngOnInit(): void {
    if (!this.customerId) {
      this.error = 'Please log in to view your dashboard.';
      this.router.navigate(['/login']);
    }
  }
}
