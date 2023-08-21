import { NgModule } from '@angular/core';
import { CommonModule, JsonPipe } from '@angular/common';

import { PosRoutingModule } from './pos-routing.module';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { PosHomeComponent } from './components/pos-home/pos-home.component';
import { ProductsComponent } from './components/products/products.component';
import { CoreModule } from 'src/app/core/core.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule, MatHint } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { ProductSimilarComponent } from './components/product-similar/product-similar.component';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NglrxPipesModule } from '@nglrx/pipes';

@NgModule({
  declarations: [
    ProductListComponent,
    ProductDetailComponent,
    PosHomeComponent,
    ProductsComponent,
    ProductSimilarComponent,
  ],
  imports: [
    CommonModule,
    PosRoutingModule,
    CoreModule,
    SharedModule,
    MatDatepickerModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    JsonPipe,
    MatTooltipModule,
    MatNativeDateModule,
    MatInputModule,
    NglrxPipesModule,
  ],
})
export class PosModule {}
