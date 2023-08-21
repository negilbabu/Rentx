import { NgModule } from '@angular/core';
// import { CommonModule } from '@angular/common';

import { VendorRoutingModule } from './vendor-routing.module';
import { VendorDashboardComponent } from './components/vendor-dashboard/vendor-dashboard.component';
import { StoreListComponent } from './components/store/store-list/store-list.component';
import { ProductListComponent } from './components/product/product-list/product-list.component';
import { VendorHomeComponent } from './components/vendor-home/vendor-home.component';
import { CoreModule } from 'src/app/core/core.module';
import { VendorOrderComponent } from './components/vendor-order/vendor-order.component';
import { VendorReportComponent } from './components/vendor-report/vendor-report.component';
import { AddStoreComponent } from './components/store/add-store/add-store.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AddProductComponent } from './components/product/add-product/add-product.component';
import { NgxUiLoaderModule } from 'ngx-ui-loader';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { VendorChangePasswordComponent } from './components/vendor-profile/vendor-profile/vendor-change-password/vendor-change-password.component';
import { MatIconModule } from '@angular/material/icon';
import { VendorProfileComponent } from './components/vendor-profile/vendor-profile/vendor-profile-detail/vendor-profile.component';
import { VendorBankDetailsComponent } from './components/vendor-profile/vendor-profile/vendor-bank-details/vendor-bank-details.component';
import { StoreDetailedViewComponent } from './components/store/store-detailed-view/store-detailed-view.component';
import { VendorProductDetailsComponent } from './components/product/vendor-product-details/vendor-product-details.component';
import { VendorOrderListComponent } from './components/order/vendor-order-list/vendor-order-list.component';
import { VendorOrderDetailViewComponent } from './components/order/vendor-order-detail-view/vendor-order-detail-view.component';


@NgModule({
  declarations: [
    VendorDashboardComponent,
    StoreListComponent,
    ProductListComponent,
    VendorHomeComponent,
    VendorOrderComponent,
    VendorReportComponent,
    AddStoreComponent,
    AddProductComponent,
    VendorProfileComponent,
    VendorChangePasswordComponent,
    VendorBankDetailsComponent,
    StoreDetailedViewComponent,
    VendorProductDetailsComponent,
    VendorOrderListComponent,
    VendorOrderDetailViewComponent,
    
  ],
  imports: [
    MatFormFieldModule,
    FormsModule,
    MatInputModule,
    MatSelectModule,MatIconModule,
    CoreModule,
    CommonModule,
    VendorRoutingModule,
    ReactiveFormsModule,
    MatTooltipModule,
    NgxUiLoaderModule,
    FormsModule,

  ],
})
export class VendorModule {}
