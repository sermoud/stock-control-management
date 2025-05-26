import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { ProductApiService, StockMovementLogDTO, StockMovementRequest, Product } from 'src/app/services/product-api.service';

@Component({
  selector: 'app-stock-movement',
  templateUrl: './stock-movement.component.html',
  styleUrls: ['./stock-movement.component.css']
})
export class StockMovementComponent implements OnInit, AfterViewInit {


  displayedColumns: string[] = [
    'productDescription',
    'changeType',
    'movedQuantity',
    'saleValue',
    'saleDate',
  ];
  dataSource: MatTableDataSource<StockMovementLogDTO>;
  isLoadingResults = true;
  errorLoadingData = false;


  entryRequest: StockMovementRequest = {
    productId: 0,
    quantity: 0,
    transactionValue: 0
  };
  exitRequest: StockMovementRequest = {
    productId: 0,
    quantity: 0,
    transactionValue: 0
  };
  isLoadingEntry = false;
  isLoadingExit = false;
  entryMessage: string | null = null;
  entryError: string | null = null;
  exitMessage: string | null = null;
  exitError: string | null = null;

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private productApiService: ProductApiService) {
    this.dataSource = new MatTableDataSource<StockMovementLogDTO>([]);
  }

  ngOnInit(): void {
    this.loadStockMovements();
  }

  loadStockMovements(): void {
    this.isLoadingResults = true;
    this.errorLoadingData = false;
    this.productApiService.getStockMovementsLog().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        this.isLoadingResults = false;
      },
      error: (err) => {
        console.error('Error fetching stock movements log', err);
        this.isLoadingResults = false;
        this.errorLoadingData = true;
        this.dataSource.data = [];
      }
    });
  }

  handleStockEntry(): void {
    if (!this.entryRequest.productId || this.entryRequest.quantity <= 0 || this.entryRequest.transactionValue < 0) {
      this.entryError = "Por favor, preencha todos os campos corretamente.";
      return;
    }
    this.isLoadingEntry = true;
    this.entryMessage = null;
    this.entryError = null;
    this.productApiService.recordStockEntry(this.entryRequest).subscribe({
      next: (product: Product) => {
        this.isLoadingEntry = false;
        this.entryMessage = `Entrada registrada com sucesso para ${product.description}. Novo estoque: ${product.quantityInStock}.`;

        this.entryRequest = { productId: 0, quantity: 0, transactionValue: 0 };
        this.loadStockMovements();
      },
      error: (err) => {
        this.isLoadingEntry = false;
        this.entryError = err.error?.message || err.message || 'Falha ao registrar entrada.';
        console.error('Erro na entrada de estoque:', err);
      }
    });
  }

  handleStockExit(): void {
    if (!this.exitRequest.productId || this.exitRequest.quantity <= 0 || this.exitRequest.transactionValue < 0) {
      this.exitError = "Por favor, preencha todos os campos corretamente.";
      return;
    }
    this.isLoadingExit = true;
    this.exitMessage = null;
    this.exitError = null;
    this.productApiService.recordStockExit(this.exitRequest).subscribe({
      next: (product: Product) => {
        this.isLoadingExit = false;
        this.exitMessage = `Saída registrada com sucesso para ${product.description}. Novo estoque: ${product.quantityInStock}.`;
        this.exitRequest = { productId: 0, quantity: 0, transactionValue: 0 };
        this.loadStockMovements();
      },
      error: (err) => {
        this.isLoadingExit = false;
        this.exitError = err.error?.message || err.message || 'Falha ao registrar saída. Verifique o estoque disponível.';
        console.error('Erro na saída de estoque:', err);
      }
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

  viewMovementDetails(movement: StockMovementLogDTO): void {
    console.log('Ver detalhes da movimentação:', movement);



  }
}
