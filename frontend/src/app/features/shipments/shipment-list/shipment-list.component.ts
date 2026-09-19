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
    'customerId',
    'status',
    'actions',
  ];

  // Mock data based on the database inserts
  dataSource = [
    { id: 1, trackingNumber: '0001', customerId: 1, status: 'DELIVERED' },
    { id: 2, trackingNumber: '0002', customerId: 1, status: 'IN_TRANSIT' },
    { id: 3, trackingNumber: '0003', customerId: 1, status: 'CREATED' },
    { id: 4, trackingNumber: '0004', customerId: 2, status: 'DELIVERED' },
  ];

  // Pagination state
  totalRecords = 4;
  pageSize = 10;
  pageIndex = 0;

  // Search control
  searchControl = new FormControl('');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit() {
    // Read URL state on load
    this.route.queryParams.subscribe((params) => {
      this.pageIndex = params['page'] ? +params['page'] : 0;
      this.pageSize = params['size'] ? +params['size'] : 10;

      if (params['search']) {
        this.searchControl.setValue(params['search'], { emitEvent: false });
      }

      this.loadShipments();
    });
  }

  loadShipments() {
    const searchTerm = this.searchControl.value;
    console.log(
      `Fetching API: page=${this.pageIndex}, size=${this.pageSize}, search=${searchTerm}`,
    );
    // TODO: HTTP call will go here
  }

  onSearch() {
    // Reset to page 0 when performing a new search, and update the URL
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { search: this.searchControl.value || null, page: 0 },
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

  viewShipment(shipmentId: number) {
    this.router.navigate(['/shipments', shipmentId]);
  }
}
