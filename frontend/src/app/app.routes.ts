import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { ShipmentListComponent } from './features/shipments/shipment-list/shipment-list.component';
import { ShipmentDetailComponent } from './features/shipments/shipment-detail/shipment-detail.component';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' }, // Default route points to Home
  { path: 'home', component: HomeComponent },
  { path: 'shipments', component: ShipmentListComponent },
  { path: 'shipments/:id', component: ShipmentDetailComponent },
];
