import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AddPayementOptionComponent } from './components/add-payement-option/add-payement-option.component';
import { ListPayementOptionComponent } from './components/list-payement-option/list-payement-option.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { MatTooltipModule } from '@angular/material/tooltip';
import { EditPaymentComponent } from './components/edit-payment/edit-payment.component';


@NgModule({
  declarations: [
    AddPayementOptionComponent,
    ListPayementOptionComponent,
    EditPaymentComponent
  ],
  imports: [
    CommonModule,ReactiveFormsModule,
    FormsModule,MatTooltipModule
  ]
})
export class PaymentModule { }
