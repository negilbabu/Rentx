
import {
  Component,
  EventEmitter,
  Inject,
  NgZone,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { EditPaymentComponent } from '../edit-payment/edit-payment.component';
import { PaymentService } from '../../services/payment.service';

@Component({
  selector: 'app-add-payement-option',
  templateUrl: './add-payement-option.component.html',
  styleUrls: ['./add-payement-option.component.css']
})
export class AddPayementOptionComponent implements OnInit {  formSubmitted = false;
  pattern = '^[A-Za-z]+( [A-Za-z]+)*$';
  minLength = 3;
  maxLength = 20;
  errorData: any;
  errorCode:any;
  showErrorMessage = false;
  addPaymentForm!: FormGroup;
  addSubcatgorySubscription!: Subscription;
  constructor(private errorCodeHandle:ApiErrorHandlerService,
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
    this.addPaymentForm = new FormGroup({
      type: new FormControl('', [
        Validators.required,
        Validators.pattern(this.pattern),
        Validators.minLength(this.minLength),
        Validators.maxLength(this.maxLength),
      ]),
    });
  }
  handleInputChange() {
    this.showErrorMessage = false; // reset flag on input change
  }
  closeDialog() {
    this.addPaymentForm.reset(); // Reset the form
    this.formSubmitted = false;
    this.dialog.closeAll();
    this.dialogRef.close();
  }
  
  onSubmit() {
    this.formSubmitted = true;
 
    if (this.addPaymentForm.valid) {
     
      
      const name = this.addPaymentForm.get('type')?.value;
      const paramBody = {
        
        type: name,
      };

      this.addSubcatgorySubscription = this.paymentService
        .addPayment(paramBody)
        .subscribe({
          next: (result: any) => {
            this.dialog.closeAll();
            this.toast.showSucessToast(
              'Payment added successfully',
              'close'
            );
            this.eventEmitter.onSaveEvent();
          },
          error: (error: any) => {
            this.errorCode = this.errorCodeHandle.getErrorMessage(
              error.error.errorCode
            );
            if (this.errorCode === '007') {
              this.route.navigateByUrl('/error');
            } else {
              this.showErrorMessage=true;
              this.errorData = this.errorCodeHandle.getErrorMessage(
                error.error.errorCode
              );
             
            }
          },
        });
    }
  }
}
