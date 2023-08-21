import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { UserPrivilegeGuard } from 'src/app/guards/user-privilege.guard';

const routes: Routes = [
 
      {
        path: 'checkout',
        component: CheckoutComponent,  canActivate: [UserPrivilegeGuard],
      },
      
      
    ];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OrderRoutingModule { }
