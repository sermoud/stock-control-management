import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { ProductProfit } from '../components/product-profit-list/product-profit-list.component';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductProfitService {

  private apiUrl = environment.apiUrl + '/products/profitability';

  constructor(private http: HttpClient) { }

  getProductProfits(): Observable<ProductProfit[]> {
    return this.http.get<ProductProfit[]>(this.apiUrl)
      .pipe(
        tap(data => console.log('Product Profits fetched:', data)),
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('API Error:', error);
    return throwError(() => new Error('Something bad happened; please try again later. Details: ' + error.message));
  }
}
