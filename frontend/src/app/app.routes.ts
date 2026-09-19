import { Routes } from '@angular/router';
import { ShipmentListComponent } from './features/shipments/shipment-list/shipment-list.component';
import { ShipmentDetailComponent } from './features/shipments/shipment-detail/shipment-detail.component';

export const routes: Routes = [
  { path: '', redirectTo: 'shipments', pathMatch: 'full' },
  { path: 'shipments', component: ShipmentListComponent },
  { path: 'shipments/:id', component: ShipmentDetailComponent },
];
