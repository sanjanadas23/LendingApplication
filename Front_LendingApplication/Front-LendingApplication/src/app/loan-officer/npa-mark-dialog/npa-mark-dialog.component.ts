import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { LoanResponseDTO } from '../models/loan-officer.model';

@Component({
  selector: 'app-npa-mark-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule
  ],
  templateUrl: './npa-mark-dialog.component.html',
  styleUrls: ['./npa-mark-dialog.component.css']
})
export class NpaMarkDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<NpaMarkDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { loan: LoanResponseDTO }
  ) { }

  submit(): void {
    this.dialogRef.close(true);
  }
}