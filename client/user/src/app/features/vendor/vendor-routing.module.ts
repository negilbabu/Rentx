import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { VendorHomeComponent } from './components/vendor-home/vendor-home.component';
import { VendorDashboardComponent } from './components/vendor-dashboard/vendor-dashboard.component';
import { ProductListComponent } from './components/product/product-list/product-list.component';
import { StoreListComponent } from './components/store/store-list/store-list.component';
import { VendorOrderComponent } from './components/vendor-order/vendor-order.component';
import { VendorReportComponent } from './components/vendor-report/vendor-report.component';
import { AddStoreComponent } from './components/store/add-store/add-store.component';
import { VendorGuard } from 'src/app/guards/vendor.guard';
import { AddProductComponent } from './components/product/add-product/add-product.component';

import { VendorChangePasswordComponent } from './components/vendor-profile/vendor-profile/vendor-change-password/vendor-change-password.component';
import { VendorProfileComponent } from './components/vendor-profile/vendor-profile/vendor-profile-detail/vendor-profile.component';
import { VendorBankDetailsComponent } from './components/vendor-profile/vendor-profile/vendor-bank-details/vendor-bank-details.component';
import { StoreDetailedViewComponent } from './components/store/store-detailed-view/store-detailed-view.component';
import { VendorProductDetailsComponent } from './components/product/vendor-product-details/vendor-product-details.component';
import { VendorOrderDetailViewComponent } from './components/order/vendor-order-detail-view/vendor-order-detail-view.component';
import { VendorOrderListComponent } from './components/order/vendor-order-list/vendor-order-list.component';

const routes: Routes = [
  {
    path: '',
    component: VendorHomeComponent,
    children: [
      {
        path: 'dashboard',
        component: VendorDashboardComponent,  canActivate: [VendorGuard],
      },
      {
        path: 'products',
        component: ProductListComponent,  canActivate: [VendorGuard],
      },
      {
        path: 'addProduct',
        component: AddProductComponent,  canActivate: [VendorGuard],
      },
      {
        path: 'stores',
        component: StoreListComponent,  canActivate: [VendorGuard],
      },
   
      {
        path: 'productView/:id',
        component: VendorProductDetailsComponent,  canActivate: [VendorGuard],
      },
      {
        path: 'addStore',
        component: AddStoreComponent,  canActivate: [VendorGuard],
      },
      {
        path: 'orders',
        component: VendorOrderComponent,  canActivate: [VendorGuard],
      },
      {
        path: 'reports',
        component: VendorReportComponent,  canActivate: [VendorGuard],
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full',
      },
      {
        path: 'profile',
        component: VendorProfileComponent,  canActivate: [VendorGuard],
      },
      {
        path: 'changePassword',
        component: VendorChangePasswordComponent,  canActivate: [VendorGuard],
      }
      ,
      {
        path: 'bankDetail',
        component: VendorBankDetailsComponent,  canActivate: [VendorGuard],
      }
      ,
      {
        path: 'storeDetail/:id',
        component: StoreDetailedViewComponent,  canActivate: [VendorGuard],
      },
      {
        path: 'VendorOrderDetail/:id',
        component: VendorOrderDetailViewComponent,  canActivate: [VendorGuard],
      },
      {
        path: 'VendorOrderList',
        component: VendorOrderListComponent,  canActivate: [VendorGuard],
      }
      
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class VendorRoutingModule {}
