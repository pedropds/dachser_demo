import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator'; // <-- Import Paginator
import { CalculationDialogComponent } from './calculate-shipment-financials-dialog/calculation-dialog.component';
import {
  CalculateFinancialsRequest,
  PaginatedFinancials,
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
    MatPaginatorModule,
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
    'calculatedAt',
  ];

  // Pagination state
  totalRecords = 0;
  pageSize = 10;
  pageIndex = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router, // <-- Inject Router
    private location: Location,
    private dialog: MatDialog,
    private financialService: FinancialService,
  ) {}

  ngOnInit() {
    this.shipmentId = this.route.snapshot.paramMap.get('id') || '';

    // Read query params for pagination
    this.route.queryParams.subscribe((params) => {
      this.pageIndex = params['page'] ? +params['page'] : 0;
      this.pageSize = params['size'] ? +params['size'] : 10;
      this.loadFinancialHistory();
    });
  }

  loadFinancialHistory() {
    this.financialService
      .getHistory(this.shipmentId, this.pageIndex, this.pageSize) // <-- Pass params
      .subscribe({
        next: (response: PaginatedFinancials) => {
          this.financialHistory = [...response.data];
          this.totalRecords = response.paginationMetadata.totalRecords; // <-- Update total records
        },
        error: (err) => console.error('Error fetching history', err),
      });
  }

  onPageChange(event: PageEvent) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page: event.pageIndex, size: event.pageSize },
      queryParamsHandling: 'merge',
      replaceUrl: true, // prevent this from pushing another entry to the router history.
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
        const requestPayload: CalculateFinancialsRequest = {
          shipmentId: Number(this.shipmentId),
          incomes: [{ amount: result.income }],
          costs: [],
          description: result.description,
        };

        if (result.baseCost > 0) {
          requestPayload.costs.push({
            costType: 'BASE_COST',
            amount: result.baseCost,
          });
        }

        if (result.additionalCost > 0) {
          requestPayload.costs.push({
            costType: 'ADDITIONAL_COST',
            amount: result.additionalCost,
          });
        }

        this.financialService.calculate(requestPayload).subscribe({
          next: (newFinancialRecord: ShipmentFinancial) => {
            // Push the new record and increment total for immediate UI feedback
            this.financialHistory = [
              newFinancialRecord,
              ...this.financialHistory,
            ];
            this.totalRecords++;
          },
          error: (err) => {
            console.error('Failed to save calculation', err);
          },
        });
      }
    });
  }
}
