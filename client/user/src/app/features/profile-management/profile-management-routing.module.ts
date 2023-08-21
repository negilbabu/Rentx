import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ListAddressComponent } from './components /list-address/list-address.component';
import { AddAddressComponent } from './components /add-address/add-address.component';
import { UserHomeComponent } from './components /user-home/user-home.component';
import { UserPrivilegeGuard } from 'src/app/guards/user-privilege.guard';
import { ChangeUserPasswordComponent } from './components /change-user-password/change-user-password.component';
import { UserProfileDetailComponent } from './components /user-profile-detail/user-profile-detail.component';
import { EditAddressComponent } from './components /edit-address/edit-address.component';
import { WishlistViewComponent } from './components /wishlist-view/wishlist-view.component';
import { OrderHistoryListComponent } from './components /order-history-list/order-history-list.component';
import { OrderHistoryDetailComponent } from './components /order-history-detail/order-history-detail.component';

const routes: Routes = [
  {
    path: '',
    component: UserHomeComponent,
    children: [
     
   { path: 'listAddress',
    component: ListAddressComponent , 
     canActivate: [UserPrivilegeGuard],
  },
  {
    path: 'addAddress',
    component: AddAddressComponent , 
     canActivate: [UserPrivilegeGuard],
  },
  {
    path: 'changePassword',
    component: ChangeUserPasswordComponent , 
     canActivate: [UserPrivilegeGuard],
  },
  {
    path: 'profileDetail',
    component: UserProfileDetailComponent , 
     canActivate: [UserPrivilegeGuard],
  },
  {
    path: 'wishlist',
    component: WishlistViewComponent , 
     canActivate: [UserPrivilegeGuard],
  },
  {
    path: 'orderHistoryList',
    component: OrderHistoryListComponent , 
     canActivate: [UserPrivilegeGuard],
  },
  {
    path: 'orderDetail/:id',
    component: OrderHistoryDetailComponent , 
     canActivate: [UserPrivilegeGuard],
  },
  {
    path: 'editAddress/:id',
    component: EditAddressComponent , 
     canActivate: [UserPrivilegeGuard],
  }
]
  

}]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProfileManagementRoutingModule { }
