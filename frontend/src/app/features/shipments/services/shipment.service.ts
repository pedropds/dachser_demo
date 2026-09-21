import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaginatedShipments } from '../models/shipment.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ShipmentService {
  // Combine the global base URL with the specific endpoint path
  private endpointUrl = `${environment.apiUrl}/customer/shipments`;

  constructor(private http: HttpClient) {}

  getShipments(
    page: number,
    size: number,
    search?: string | null,
  ): Observable<PaginatedShipments> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search) {
      params = params.set('filter.search', search);
    }

    return this.http.get<PaginatedShipments>(this.endpointUrl, { params });
  }
}
