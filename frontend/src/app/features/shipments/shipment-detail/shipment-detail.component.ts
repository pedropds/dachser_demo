import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CalculationDialogComponent } from './calculate-shipment-financials-dialog/calculation-dialog.component';

@Component({
  selector: 'app-shipment-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatTableModule,
    MatDialogModule,
  ],
  templateUrl: './shipment-detail.component.html',
  styleUrl: './shipment-detail.component.scss',
})
export class ShipmentDetailComponent implements OnInit {
  shipmentId: string = '';

  financialHistory = [
    { income: 1000, totalCosts: 200, profitOrLoss: 800 },
    { income: 500, totalCosts: 900, profitOrLoss: -400 },
  ];

  displayedColumns: string[] = ['income', 'totalCosts', 'profitOrLoss'];

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private dialog: MatDialog, // <-- Inject MatDialog
  ) {}

  ngOnInit() {
    this.shipmentId = this.route.snapshot.paramMap.get('id') || '';
  }

  goBack() {
    this.location.back();
  }

  openCalculationDialog() {
    const dialogRef = this.dialog.open(CalculationDialogComponent, {
      width: '400px',
      disableClose: true, // Forces the user to click cancel or submit
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        // Result contains the data from the dialog's form
        const totalIncome = result.income || 0;
        const totalCosts =
          (result.baseCost || 0) + (result.additionalCost || 0);
        const profitOrLoss = totalIncome - totalCosts;

        // Prepend the new calculation to the table
        this.financialHistory = [
          {
            income: totalIncome,
            totalCosts: totalCosts,
            profitOrLoss: profitOrLoss,
          },
          ...this.financialHistory,
        ];

        // TODO call the backend here
      }
    });
  }
}
