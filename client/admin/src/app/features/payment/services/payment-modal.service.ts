import { Injectable } from '@angular/core';
import { AddPayementOptionComponent } from '../../payment/components/add-payement-option/add-payement-option.component';
import { MatDialog } from '@angular/material/dialog';
import { EditPaymentComponent } from '../../payment/components/edit-payment/edit-payment.component';

@Injectable({
  providedIn: 'root'
})
export class PaymentModalService {

  constructor(public dialog: MatDialog) { }
  addPayment() {
    const alertRef = this.dialog.open(AddPayementOptionComponent, {
      panelClass: 'my-dialog-container',
    });
    return alertRef.afterClosed();
  }
  editPayment(id: number) {
    const alertRef = this.dialog.open(EditPaymentComponent, {
      panelClass: 'my-dialog-container',
      data: {
        paymentId: id,
      },
    });
    return alertRef.afterClosed();
  }
}
