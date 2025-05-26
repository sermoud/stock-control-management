import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StockMovementComponent } from './components/stock-movement/stock-movement.component';
import { ProductTypeQueryComponent } from './components/product-type-query/product-type-query.component';
import { ProductProfitListComponent } from './components/product-profit-list/product-profit-list.component';
import { ProductManagementComponent } from './components/product-management/product-management.component';

const routes: Routes = [
  { path: 'stockMovements', component: StockMovementComponent },
  { path: 'productTypeQuery', component: ProductTypeQueryComponent },
  { path: 'profitability', component: ProductProfitListComponent },
    { path: 'productManagement', component: ProductManagementComponent },
  { path: '', redirectTo: '/profitability', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
