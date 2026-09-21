import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { ShipmentService } from '../services/shipment.service';
import { Shipment } from '../models/shipment.model';

@Component({
  selector: 'app-shipment-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatCardModule,
  ],
  templateUrl: './shipment-list.component.html',
  styleUrl: './shipment-list.component.scss',
})
export class ShipmentListComponent implements OnInit {
  displayedColumns: string[] = [
    'trackingNumber',
    'customerName',
    'customerId',
    'status',
    'actions',
  ];

  dataSource: Shipment[] = [];

  // Pagination state
  totalRecords = 0;
  pageSize = 10;
  pageIndex = 0;

  // Search control
  searchControl = new FormControl('');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private shipmentService: ShipmentService,
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      this.pageIndex = params['page'] ? +params['page'] : 0;
      this.pageSize = params['size'] ? +params['size'] : 10;
      this.searchControl.setValue(params['filter.search'], {
        emitEvent: false,
      });

      this.loadShipments();
    });
  }

  loadShipments() {
    const searchTerm = this.searchControl.value;

    this.shipmentService
      .getShipments(this.pageIndex, this.pageSize, searchTerm)
      .subscribe({
        next: (response) => {
          this.dataSource = response.data;
          this.totalRecords = response.paginationMetadata.totalRecords;
        },
        error: (err) => {
          console.error('Failed to load shipments', err);
        },
      });
  }

  onSearch() {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        'filter.search': this.searchControl.value || null,
        page: 0,
      },
      queryParamsHandling: 'merge',
    });
  }

  onPageChange(event: PageEvent) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page: event.pageIndex, size: event.pageSize },
      queryParamsHandling: 'merge',
    });
  }

  viewShipment(trackingNumber: string) {
    this.router.navigate(['/shipments', trackingNumber]);
  }
}
