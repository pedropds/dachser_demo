import { inject, Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import {
  CustomSnackbarComponent,
  CustomSnackBarData,
} from '../snack-bar-message/snack-bar.component';

@Injectable({
  providedIn: 'root',
})
export class SnackBarService {
  private snackBar = inject(MatSnackBar);

  public openError(
    message: string,
    action?: string,
    config?: MatSnackBarConfig<any>,
  ) {
    this.openFromCustomComponent(message, 'error', action, config);
  }

  public openSuccess(
    message: string,
    action?: string,
    config?: MatSnackBarConfig<any>,
  ) {
    this.openFromCustomComponent(message, 'success', action, config);
  }

  private openFromCustomComponent(
    message: string,
    type: 'success' | 'error',
    action?: string,
    config?: MatSnackBarConfig<any>,
  ) {
    const existingClasses = config?.panelClass
      ? Array.isArray(config.panelClass)
        ? config.panelClass
        : [config.panelClass]
      : [];

    const customConfig: MatSnackBarConfig<CustomSnackBarData> = {
      ...config,
      panelClass: ['custom-snackbar-container', ...existingClasses],
      data: {
        message,
        action,
        type,
        ...(config?.data || {}),
      },
    };

    this.snackBar.openFromComponent(CustomSnackbarComponent, customConfig);
  }
}
