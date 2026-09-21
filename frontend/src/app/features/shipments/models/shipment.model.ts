export interface Shipment {
  id: number;
  trackingNumber: string;
  customerId: number;
  customerName: string;
  description: string;
  status: string;
}

export interface PaginationMetadata {
  page: number;
  totalPages: number;
  totalRecords: number;
}

export interface PaginatedShipments {
  data: Shipment[];
  paginationMetadata: PaginationMetadata;
}
