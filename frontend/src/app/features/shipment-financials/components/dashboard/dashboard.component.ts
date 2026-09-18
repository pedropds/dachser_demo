import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  // Temporary mock data to verify UI setup
  displayedColumns: string[] = [
    'shipmentId',
    'income',
    'totalCosts',
    'profitOrLoss',
  ];
  dataSource = [
    { shipmentId: 1, income: 500.0, totalCosts: 250.0, profitOrLoss: 250.0 },
  ];
}
