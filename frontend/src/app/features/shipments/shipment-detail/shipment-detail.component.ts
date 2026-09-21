import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CalculationDialogComponent } from './calculate-shipment-financials-dialog/calculation-dialog.component';
import {
  CalculateFinancialsRequest,
  ShipmentFinancial,
} from '../../shipment-financials/models/financial.model';
import { FinancialService } from '../../shipment-financials/services/financial.service';

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
  financialHistory: ShipmentFinancial[] = [];

  displayedColumns: string[] = [
    'income',
    'totalCosts',
    'profitOrLoss',
    'description',
  ];

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private dialog: MatDialog,
    private financialService: FinancialService,
  ) {}

  ngOnInit() {
    this.shipmentId = this.route.snapshot.paramMap.get('id') || '';
    this.loadFinancialHistory();
  }

  loadFinancialHistory() {
    this.financialService.getHistory(this.shipmentId).subscribe({
      next: (response) => {
        this.financialHistory = [...response.data];
      },
      error: (err) => console.error('Error fetching history', err),
    });
  }

  goBack() {
    this.location.back();
  }

  openCalculationDialog() {
    const dialogRef = this.dialog.open(CalculationDialogComponent, {
      width: '400px',
      disableClose: true,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        // Map to the exact structure expected by the backend record
        const requestPayload: CalculateFinancialsRequest = {
          shipmentId: Number(this.shipmentId), // Convert URL param to a number
          incomes: [{ amount: result.income }],
          costs: [],
          description: result.description,
        };

        // Add base cost
        if (result.baseCost > 0) {
          requestPayload.costs.push({
            costType: 'BASE_COST',
            amount: result.baseCost,
          });
        }

        // Conditionally add additional cost if the user provided one
        if (result.additionalCost > 0) {
          requestPayload.costs.push({
            costType: 'ADDITIONAL_COST',
            amount: result.additionalCost,
          });
        }

        this.financialService.calculate(requestPayload).subscribe({
          next: (newFinancialRecord) => {
            // Push the new record to the top of the table seamlessly
            this.financialHistory = [
              newFinancialRecord,
              ...this.financialHistory,
            ];
          },
          error: (err) => {
            console.error('Failed to save calculation', err);
          },
        });
      }
    });
  }
}
