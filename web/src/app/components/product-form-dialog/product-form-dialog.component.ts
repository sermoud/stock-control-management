import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Product, ProductType } from 'src/app/services/product-api.service';

export interface ProductDialogData {
  product?: Product;
  isEditMode: boolean;
}

@Component({
  selector: 'app-product-form-dialog',
  templateUrl: './product-form-dialog.component.html',
  styleUrls: ['./product-form-dialog.component.css']
})
export class ProductFormDialogComponent implements OnInit {
  productForm: FormGroup;
  productTypes = Object.values(ProductType);
  isEditMode: boolean;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ProductFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ProductDialogData
  ) {
    this.isEditMode = data.isEditMode;
    this.productForm = this.fb.group({
      id: [null],
      code: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(5)]],
      productType: [null, Validators.required],
      supplierValue: [null, [Validators.required, Validators.min(0.01)]],
      quantityInStock: [{ value: 0, disabled: true }]
    });

    if (data.product && this.isEditMode) {
      this.productForm.patchValue(data.product);
    }
  }

  ngOnInit(): void {}

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (this.productForm.valid) {


      const formData = this.productForm.getRawValue();
      if (!this.isEditMode) {

        delete formData.id;
        delete formData.quantityInStock;
      }
      this.dialogRef.close(formData);
    } else {
      this.productForm.markAllAsTouched();
    }
  }
}
