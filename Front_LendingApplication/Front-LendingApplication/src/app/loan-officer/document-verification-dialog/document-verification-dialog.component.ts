import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { CommonModule } from '@angular/common';
import { DocumentResponseDTO } from '../models/loan-officer.model';
import { LoanOfficerService } from '../services/loan-officer.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-document-verification-dialog',
  standalone: true,
  imports: [
    CommonModule, // Added CommonModule for NgIf
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    ReactiveFormsModule
  ],
  templateUrl: './document-verification-dialog.component.html',
  styleUrls: ['./document-verification-dialog.component.css']
})
export class DocumentVerificationDialogComponent {
  verifyForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<DocumentVerificationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { document: DocumentResponseDTO },
    private loanOfficerService: LoanOfficerService,
    private fb: FormBuilder
  ) {
    this.verifyForm = this.fb.group({
      status: ['', Validators.required]
    });
  }

  submit(): void {
    if (this.verifyForm.valid) {
      const status = this.verifyForm.value.status;
      this.loanOfficerService.verifyDocument(this.data.document.documentId, { status }).subscribe({
        next: (doc) => this.dialogRef.close(doc),
        error: (err) => this.dialogRef.close({ error: `Failed to verify: ${err.message}` })
      });
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }
}

// import { Component, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { MatTableModule } from '@angular/material/table';
// import { MatCardModule } from '@angular/material/card';
// import { MatButtonModule } from '@angular/material/button';
// import { MatDialog, MatDialogModule } from '@angular/material/dialog';
// import { MatTableDataSource } from '@angular/material/table';
// import { LoanOfficerService } from '../services/loan-officer.service';
// import { DocumentResponseDTO } from '../models/loan-officer.model';
// import { ActivatedRoute } from '@angular/router';
// // import { DocumentVerificationDialogComponent } from '../document-verification-dialog/document-verification-dialog.component';
// import { DocumentVerificationDialogComponent } from '../document-verification/document-verification.component';

// @Component({
//   selector: 'app-document-verification',
//   standalone: true,
//   imports: [
//     CommonModule,
//     MatTableModule,
//     MatCardModule,
//     MatButtonModule,
//     MatDialogModule
//   ],
//   templateUrl: './document-verification.component.html',
//   styleUrls: ['./document-verification.component.css']
// })
// export class DocumentVerificationComponent implements OnInit {
//   documents = new MatTableDataSource<DocumentResponseDTO>([]);
//   errorMessage: string | null = null;
//   displayedColumns: string[] = ['documentId', 'documentName', 'documentType', 'viewDocument', 'status', 'actions'];

//   constructor(
//     private loanOfficerService: LoanOfficerService,
//     private route: ActivatedRoute,
//     private dialog: MatDialog
//   ) { }

//   ngOnInit(): void {
//     const loanId = this.route.snapshot.paramMap.get('loanId');
//     if (loanId) {
//       this.loadDocuments(+loanId);
//     } else {
//       this.errorMessage = 'Invalid loan ID.';
//     }
//   }

//   loadDocuments(loanId: number): void {
//     this.errorMessage = null;
//     this.loanOfficerService.getDocumentsByLoanId(loanId).subscribe({
//       next: (documents: DocumentResponseDTO[]) => {
//         this.documents.data = documents;
//         if (documents.length === 0) {
//           this.errorMessage = 'No documents found for this loan.';
//         }
//       },
//       error: (err: Error) => {
//         console.error('Failed to load documents:', err);
//         this.errorMessage = 'Failed to load documents. Please try again later.';
//       }
//     });
//   }

//   verifyDocument(document: DocumentResponseDTO): void {
//     this.openVerificationDialog(document);
//   }

//   openVerificationDialog(document: DocumentResponseDTO): void {
//     const dialogRef = this.dialog.open(DocumentVerificationDialogComponent, {
//       data: { document }
//     });

//     dialogRef.afterClosed().subscribe(result => {
//       if (result && !result.error) {
//         this.documents.data = this.documents.data.map(doc =>
//           doc.documentId === result.documentId ? result : doc
//         );
//       } else if (result?.error) {
//         this.errorMessage = result.error;
//       }
//     });
//   }
// }