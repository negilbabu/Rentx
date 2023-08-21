import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import {
  MatDialogRef,
  MatDialog,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { PaymentService } from '../../services/payment.service';
import { AddPayementOptionComponent } from '../add-payement-option/add-payement-option.component';

@Component({
  selector: 'app-edit-payment',
  templateUrl: './edit-payment.component.html',
  styleUrls: ['./edit-payment.component.css'],
})
export class EditPaymentComponent implements OnInit {
  pattern = '^[A-Za-z]+( [A-Za-z]+)*$';
  minLength = 3;
  maxLength = 20;
  errorData: any;
  errorCode: any;
  formSubmitted = false;
  showErrorMessage = false;

  updatePaymentForm!: FormGroup;
  addSubcatgorySubscription!: Subscription;
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { paymentId: any },
    private errorCodeHandle: ApiErrorHandlerService,
    private eventEmitter: EventEmitterService,
    private dialogRef: MatDialogRef<AddPayementOptionComponent>,
    private paymentService: PaymentService,
    private route: Router,
    private dialog: MatDialog,
    public toast: ToastService
  ) {}
  ngOnDestroy(): void {
    this.dialog.closeAll();
  }

  ngOnInit(): void {
    this.updatePaymentForm = new FormGroup({
      type: new FormControl('', [
        Validators.required,
        Validators.pattern(this.pattern),
        Validators.minLength(this.minLength),
        Validators.maxLength(this.maxLength),
      ]),
    });
    this.getPaymentDetail();
  }
  getPaymentDetail() {
    this.paymentService.getPaymentDetail(this.paymentId).subscribe({
      next: (result: any) => {
        this.updatePaymentForm.patchValue({
          type: result.name
        });
      }
    });
  }
  
  handleInputChange() {
    this.showErrorMessage = false; // reset flag on input change
  }
  get paymentId(): number {
    return this.data.paymentId;
  }
  closeDialog() {
    this.updatePaymentForm.reset(); // Reset the form
    this.formSubmitted = false;
    this.dialog.closeAll();
    this.dialogRef.close();
  }

  onSubmit() {
    
    this.formSubmitted = true;

    if (this.updatePaymentForm.valid) {
      const name = this.updatePaymentForm.get('type')?.value;
      const paramBody = {
        type: name,
      };

      this.addSubcatgorySubscription = this.paymentService
        .updatePayment(this.updatePaymentForm.value, this.paymentId)
        .subscribe({
          next: (result: any) => {
            this.dialog.closeAll();
            this.toast.showSucessToast('Payment updated successfully', 'close');
            this.eventEmitter.onSaveEvent();
          },
          error: (error: any) => {
            this.errorCode = this.errorCodeHandle.getErrorMessage(
              error.error.errorCode
            );
            if (this.errorCode === '007') {
              this.route.navigateByUrl('/error');
            } else {
              this.showErrorMessage = true;
              this.errorData = this.errorCodeHandle.getErrorMessage(
                error.error.errorCode
              );
            }
          },
        });
    }
  }
}
