import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  ShipmentFinancial,
  CalculateFinancialsRequest,
  PaginatedFinancials,
} from '../models/financial.model';

@Injectable({
  providedIn: 'root',
})
export class FinancialService {
  private endpointUrl = `${environment.apiUrl}/financials/shipments`;

  constructor(private http: HttpClient) {}

  // Now returns the wrapper object
  getHistory(trackingNumber: string): Observable<PaginatedFinancials> {
    return this.http.get<PaginatedFinancials>(
      `${this.endpointUrl}/${trackingNumber}`,
    );
  }

  calculate(
    request: CalculateFinancialsRequest,
  ): Observable<ShipmentFinancial> {
    return this.http.post<ShipmentFinancial>(
      `${this.endpointUrl}/calculate`,
      request,
    );
  }
}
