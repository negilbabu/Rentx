import { NgModule } from '@angular/core';
import { CommonModule, JsonPipe } from '@angular/common';

import { OrderRoutingModule } from './order-routing.module';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { CoreModule } from 'src/app/core/core.module';
import { SharedModule } from 'src/app/shared/shared.module';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import {MatExpansionModule} from '@angular/material/expansion';
import { CartModule } from '../cart/cart.module';
import { ProfileManagementModule } from '../profile-management/profile-management.module';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';


@NgModule({
  declarations: [
    CheckoutComponent,

  ],
  imports: [
    CommonModule,
    OrderRoutingModule,
    CoreModule,
    SharedModule,
    MatDatepickerModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    JsonPipe,
    MatNativeDateModule,
    MatInputModule,
    CdkAccordionModule,
    MatExpansionModule,
    CartModule,
    ProfileManagementModule,
    MatButtonModule,
    MatMenuModule
  



    
  ]
})
export class OrderModule { }
