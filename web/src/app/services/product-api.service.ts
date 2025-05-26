import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';



export interface ProductStockInfoDTO {
  productCode: string;
  productDescription: string;
  quantitySold: number;
  quantityAvailable: number;
}

export interface ProductProfitDTO {
  productCode: string;
  productDescription: string;
  totalQuantitySold: number;
    totalSaleValue: number;
  totalSupplierCost: number;
  totalProfit: number;
}

export interface StockMovementRequest {
  productId: number;
  quantity: number;
  transactionValue: number;
}

export interface Product {
  id: number;
  code: string;
  description: string;
  productType: string;
  supplierValue: number;
  quantityInStock: number;
}

export interface StockMovementLogDTO {
  id: number;
  product: Product;
  changeType: 'ENTRY' | 'EXIT';
  quantityChanged: number;
  value: number;
  date: string;


}

export enum ProductType {
  ELECTRONIC = 'ELECTRONIC',
  APPLIANCE = 'APPLIANCE',
  FURNITURE = 'FURNITURE',
  OTHER = 'OTHER'
}

@Injectable({
  providedIn: 'root'
})
export class ProductApiService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }


  getProductProfitability(): Observable<ProductProfitDTO[]> {
    return this.http.get<ProductProfitDTO[]>(`${this.baseUrl}/products/profitability`);
  }


  getProductsByType(productType: string): Observable<ProductStockInfoDTO[]> {
    return this.http.get<ProductStockInfoDTO[]>(`${this.baseUrl}/products/by-type/${productType}`);
  }


  recordStockEntry(request: StockMovementRequest): Observable<Product> {
    return this.http.post<Product>(`${this.baseUrl}/stock/entry`, request);
  }


  recordStockExit(request: StockMovementRequest): Observable<Product> {
    return this.http.post<Product>(`${this.baseUrl}/stock/exit`, request);
  }


  getStockMovementsLog(): Observable<StockMovementLogDTO[]> {
    return this.http.get<StockMovementLogDTO[]>(`${this.baseUrl}/stock/movements-history`);
  }


  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/products`);
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/products/${id}`);
  }

  createProduct(product: Omit<Product, 'id' | 'quantityInStock'>): Observable<Product> {

    const productToCreate = { ...product, quantityInStock: 0 };
    return this.http.post<Product>(`${this.baseUrl}/products`, productToCreate);
  }

  updateProduct(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.baseUrl}/products/${id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/products/${id}`);
  }



}
