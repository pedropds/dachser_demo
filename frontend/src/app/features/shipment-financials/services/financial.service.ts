import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
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

  getHistory(
    trackingNumber: string,
    page: number,
    size: number,
  ): Observable<PaginatedFinancials> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PaginatedFinancials>(
      `${this.endpointUrl}/${trackingNumber}`,
      { params },
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
