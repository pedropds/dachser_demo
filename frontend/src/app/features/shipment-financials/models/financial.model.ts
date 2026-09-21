export interface ShipmentFinancial {
  id: number;
  shipmentId: number;
  description: string;
  income: number;
  totalCosts: number;
  profitOrLoss: number;
  calculatedAt: string;
}

export interface IncomeEntry {
  amount: number;
}

export interface CostEntry {
  costType: string;
  amount: number;
}

export interface CalculateFinancialsRequest {
  shipmentId: number;
  incomes: IncomeEntry[];
  costs: CostEntry[];
  description: string;
}

export interface PaginationMetadata {
  page: number;
  totalPages: number;
  totalRecords: number;
}

export interface PaginatedFinancials {
  data: ShipmentFinancial[];
  paginationMetadata: PaginationMetadata;
}
