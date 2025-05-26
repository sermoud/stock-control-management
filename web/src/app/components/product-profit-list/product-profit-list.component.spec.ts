import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductProfitListComponent } from './product-profit-list.component';

describe('ProductProfitListComponent', () => {
  let component: ProductProfitListComponent;
  let fixture: ComponentFixture<ProductProfitListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductProfitListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductProfitListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
