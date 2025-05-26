import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { Product, ProductApiService } from 'src/app/services/product-api.service';
import { ProductFormDialogComponent, ProductDialogData } from '../product-form-dialog/product-form-dialog.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-product-management',
  templateUrl: './product-management.component.html',
  styleUrls: ['./product-management.component.css']
})
export class ProductManagementComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['code', 'description', 'productType', 'supplierValue', 'quantityInStock', 'actions'];
  dataSource: MatTableDataSource<Product>;
  isLoadingResults = true;
  errorLoadingData = false;

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private productApiService: ProductApiService,
    public dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {
    this.dataSource = new MatTableDataSource<Product>();
  }

  ngOnInit(): void {
    this.loadProducts();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  loadProducts(): void {
    this.isLoadingResults = true;
    this.errorLoadingData = false;
    this.productApiService.getAllProducts().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.isLoadingResults = false;
      },
      error: (err) => {
        console.error('Erro ao carregar produtos:', err);
        this.errorLoadingData = true;
        this.isLoadingResults = false;
        this.snackBar.open('Erro ao carregar produtos.', 'Fechar', { duration: 3000 });
      }
    });
  }

  openProductDialog(product?: Product): void {
    const dialogData: ProductDialogData = {
      product: product ? { ...product } : undefined,
      isEditMode: !!product
    };

    const dialogRef = this.dialog.open(ProductFormDialogComponent, {
      width: '450px',
      data: dialogData
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (dialogData.isEditMode && product) {

          this.productApiService.updateProduct(product.id, result).subscribe({
            next: () => {
              this.snackBar.open('Produto atualizado com sucesso!', 'Fechar', { duration: 3000 });
              this.loadProducts();
            },
            error: (err) => this.snackBar.open(`Erro ao atualizar produto: ${err.error?.message || err.message}`, 'Fechar', { duration: 3000 })
          });
        } else {

          this.productApiService.createProduct(result).subscribe({
            next: () => {
              this.snackBar.open('Produto criado com sucesso!', 'Fechar', { duration: 3000 });
              this.loadProducts();
            },
            error: (err) => this.snackBar.open(`Erro ao criar produto: ${err.error?.message || err.message}`, 'Fechar', { duration: 3000 })
          });
        }
      }
    });
  }

  deleteProduct(productId: number): void {
    if (confirm('Tem certeza que deseja excluir este produto?')) {
      this.productApiService.deleteProduct(productId).subscribe({
        next: () => {
          this.snackBar.open('Produto excluído com sucesso!', 'Fechar', { duration: 3000 });
          this.loadProducts();
        },
        error: (err) => this.snackBar.open(`Erro ao excluir produto: ${err.error?.message || err.message}`, 'Fechar', { duration: 3000 })
      });
    }
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
}
