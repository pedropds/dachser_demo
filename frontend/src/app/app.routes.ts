import { Routes } from '@angular/router';
import { ShipmentListComponent } from './features/shipments/shipment-list/shipment-list.component';

export const routes: Routes = [
  { path: '', redirectTo: 'shipments', pathMatch: 'full' },
  { path: 'shipments', component: ShipmentListComponent },
];
