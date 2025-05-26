import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { ProductProfitService } from 'src/app/services/product-profit.service';

export interface ProductProfit {
  productDescription: string;
  totalQuantitySold: number;
  productCode: string;
  totalProfit: number;
}

@Component({
  selector: 'app-product-profit-list',
  templateUrl: './product-profit-list.component.html',
  styleUrls: ['./product-profit-list.component.css'],
})
export class ProductProfitListComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = [
    'productCode',
    'productDescription',
    'totalQuantitySold',
    'totalSaleValue',
    'totalSupplierCost',
    'totalProfit',
  ];
  dataSource: MatTableDataSource<ProductProfit>;
  isLoadingResults = true;
  errorLoadingData = false;

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private productProfitService: ProductProfitService) {
    this.dataSource = new MatTableDataSource<ProductProfit>([]);
  }

  ngOnInit(): void {
    this.loadProductProfits();
  }

  loadProductProfits(): void {
    this.isLoadingResults = true;
    this.errorLoadingData = false;
    this.productProfitService.getProductProfits().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.isLoadingResults = false;
      },
      error: (err) => {
        console.error('Error fetching product profits', err);
        this.isLoadingResults = false;
        this.errorLoadingData = true;
        this.dataSource.data = [];
      },
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  clearInputAndFilter(inputElement: HTMLInputElement): void {
    inputElement.value = '';
    this.applyFilter({ target: inputElement } as unknown as Event);
  }

  viewDetails(item: ProductProfit): void {
    console.log('Ver detalhes:', item);
  }
}
