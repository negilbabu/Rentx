import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserManagementModule } from './user-management/user-management.module';
import { VendorModule } from './vendor/vendor.module';
import { PosModule } from './pos/pos.module';
import { CartModule } from './cart/cart.module';
import { CoreModule } from '../core/core.module';
import { MatTooltipModule } from '@angular/material/tooltip';
import { OrderModule } from './order/order.module';
import { ProfileManagementModule } from './profile-management/profile-management.module';


@NgModule({
  declarations: [],
  imports: [CommonModule, MatTooltipModule,UserManagementModule,CoreModule,VendorModule, OrderModule,
     PosModule,CartModule,ProfileManagementModule],
})
export class FeaturesModule {}

