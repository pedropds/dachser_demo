import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-calculation-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  templateUrl: './calculation-dialog.component.html',
  styleUrl: './calculation-dialog.component.scss',
})
export class CalculationDialogComponent {
  calcForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CalculationDialogComponent>,
  ) {
    this.calcForm = this.fb.group({
      income: [null, [Validators.required, Validators.min(0)]],
      baseCost: [null, [Validators.required, Validators.min(0)]],
      additionalCost: [null, [Validators.min(0)]],
      description: [null, [Validators.required, Validators.minLength(5)]],
    });
  }

  submit() {
    if (this.calcForm.valid) {
      this.dialogRef.close(this.calcForm.value);
    }
  }
}
