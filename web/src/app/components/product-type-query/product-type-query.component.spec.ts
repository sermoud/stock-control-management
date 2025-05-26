import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductTypeQueryComponent } from './product-type-query.component';

describe('ProductTypeQueryComponent', () => {
  let component: ProductTypeQueryComponent;
  let fixture: ComponentFixture<ProductTypeQueryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductTypeQueryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductTypeQueryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
