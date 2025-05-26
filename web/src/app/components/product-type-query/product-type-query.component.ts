import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { ProductApiService, ProductStockInfoDTO, ProductType } from 'src/app/services/product-api.service';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-product-type-query',
  templateUrl: './product-type-query.component.html',
  styleUrls: ['./product-type-query.component.css']
})
export class ProductTypeQueryComponent implements OnInit, AfterViewInit {
  selectedProductType: ProductType | '' = '';
  productTypes = Object.values(ProductType);

  displayedProductColumns: string[] = ['productCode', 'productDescription', 'quantitySold', 'quantityAvailable'];
  productsDataSource: MatTableDataSource<ProductStockInfoDTO>;

  isLoading = false;
  error: string = "";

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private productApiService: ProductApiService,
    private snackBar: MatSnackBar
  ) {
    this.productsDataSource = new MatTableDataSource<ProductStockInfoDTO>();
  }

  ngOnInit(): void {

  }

  ngAfterViewInit(): void {
    this.productsDataSource.sort = this.sort;
    this.productsDataSource.paginator = this.paginator;
  }

  fetchProductsByType(): void {
    if (!this.selectedProductType) {
      this.error = "Por favor, selecione um tipo de produto.";
      this.productsDataSource.data = [];
      return;
    }
    this.isLoading = true;
    this.error = "";
    this.productsDataSource.data = [];

    this.productApiService.getProductsByType(this.selectedProductType as string).subscribe({
      next: (data) => {
        this.productsDataSource.data = data;
        this.isLoading = false;
        if (data.length === 0) {
          this.snackBar.open('Nenhum produto encontrado para este tipo.', 'Fechar', { duration: 3000 });
        }
      },
      error: (err) => {
        this.error = err.error?.message || err.message || 'Falha ao buscar produtos por tipo.';
        this.isLoading = false;
        console.error('Erro ao buscar produtos por tipo:', err);
        this.snackBar.open(this.error, 'Fechar', { duration: 3000 });
       }
     });
   }
 }
