import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import {
  MAT_SNACK_BAR_DATA,
  MatSnackBarRef,
} from '@angular/material/snack-bar';

export interface CustomSnackBarData {
  message: string;
  action?: string;
  type: 'success' | 'error';
}

@Component({
  selector: 'app-custom-snackbar',
  templateUrl: './snack-bar.component.html',
  styleUrls: ['./snack-bar.component.scss'],
  standalone: true,
  imports: [CommonModule],
})
export class CustomSnackbarComponent {
  constructor(
    @Inject(MAT_SNACK_BAR_DATA) public data: CustomSnackBarData,
    public snackBarRef: MatSnackBarRef<CustomSnackbarComponent>,
  ) {}
}
