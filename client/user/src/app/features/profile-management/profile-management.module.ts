import { NgModule } from '@angular/core';
import { CommonModule, JsonPipe } from '@angular/common';

import { ProfileManagementRoutingModule } from './profile-management-routing.module';
import { AddAddressComponent } from './components /add-address/add-address.component';
import { CoreModule } from 'src/app/core/core.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { SharedModule } from 'src/app/shared/shared.module';
import { CartRoutingModule } from '../cart/cart-routing.module';

import {MatCardModule} from '@angular/material/card';
import { ListAddressComponent } from './components /list-address/list-address.component';
import { MatIconModule } from '@angular/material/icon';
import { UserHomeComponent } from './components /user-home/user-home.component';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ChangeUserPasswordComponent } from './components /change-user-password/change-user-password.component';
import { UserProfileDetailComponent } from './components /user-profile-detail/user-profile-detail.component';
import { EditAddressComponent } from './components /edit-address/edit-address.component';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import { WishlistViewComponent } from './components /wishlist-view/wishlist-view.component';
import { OrderHistoryListComponent } from './components /order-history-list/order-history-list.component';
import { OrderHistoryDetailComponent } from './components /order-history-detail/order-history-detail.component';
@NgModule({
  declarations: [
    AddAddressComponent,
    ListAddressComponent,
    UserHomeComponent,
    ChangeUserPasswordComponent,
    UserProfileDetailComponent,
    EditAddressComponent,
    WishlistViewComponent,
    OrderHistoryListComponent,
    OrderHistoryDetailComponent,

  ],
  imports:    [
  CommonModule, 
  ProfileManagementRoutingModule,
  CoreModule,
  SharedModule,MatTooltipModule,
  MatDatepickerModule,
  MatFormFieldModule,
  FormsModule,
  ReactiveFormsModule,
  JsonPipe,
  MatNativeDateModule,
  MatInputModule,
  CartRoutingModule,
  MatCardModule,
  MatIconModule,
  MatTooltipModule,
  MatButtonModule,
  MatMenuModule


  
],
exports: [AddAddressComponent],
})
export class ProfileManagementModule { }