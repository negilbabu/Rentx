import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { VendorProfileService } from 'src/app/features/vendor/services/vendor-profile.service';
import { ToastService } from 'src/app/shared/services/toast.service';

@Component({
  selector: 'app-vendor-bank-details',
  templateUrl: './vendor-bank-details.component.html',
  styleUrls: ['./vendor-bank-details.component.css'],
})
export class VendorBankDetailsComponent implements OnInit {
  iseditable: true | false = true;
  formSubmitted = false;
  buttonDisabler = false;
  accountHolderNameRegexp = new RegExp(/^[a-zA-Z]+$/);
  onlyNumberRegexp = new RegExp(/^[0-9]+$/);
  ifscCodeRegexp = new RegExp(/^[A-Z0-9]{11}$/);
  gstNoRegexp = new RegExp(/^[A-Z0-9]{15}$/);
  panNoRegexp = new RegExp(/^[A-Z0-9]{10}$/);
  vendorBankDetailForm!: FormGroup;
  constructor(
    private profileService: VendorProfileService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.getBankDetail();
    this.vendorBankDetailForm = new FormGroup({
      holderName: new FormControl('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(50),
        Validators.pattern(this.accountHolderNameRegexp),
      ]),
      accountNumber: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(18),
        Validators.pattern(this.onlyNumberRegexp),
      ]),
      ifsc: new FormControl('', [
        Validators.required,
        Validators.pattern(this.ifscCodeRegexp),
      ]),
      gst: new FormControl('', [
        Validators.required,
        Validators.pattern(this.gstNoRegexp),
      ]),
      pan: new FormControl('', [
        Validators.required,
        Validators.pattern(this.panNoRegexp),
      ]),
    });
  }
  getBankDetail() {
    this.profileService.getVendorBankDetails().subscribe({
      next: (data: any) => {
        this.vendorBankDetailForm.patchValue({
          accountNumber:data.accountNumber,
          holderName: data.holderName,
          ifsc: data.ifsc,
          gst: data.gst,
          pan: data.pan,
        });
      },
      error: () => {},
    });
  }
  onSubmitBankDetails() {
    this.formSubmitted = true;
    if (this.vendorBankDetailForm.valid) {
      this.buttonDisabler = true;
      this.profileService
        .updateVendorBankDetails(this.vendorBankDetailForm.value)
        .subscribe({
          next: (data: any) => {
            this.buttonDisabler = false;
            this.toastService.showSucessToast(
              'Bank detail updated successfully',
              'close'
            );
            this.iseditable = this.toggleEditOrView(this.iseditable);
            this.getBankDetail();
          },
          error: () => {},
        });
    }
  }
  toggleEditOrView(iseditable: true | false): true | false {
    return this.iseditable === true ? false : true;
  }
  editHandle() {
    this.iseditable = this.toggleEditOrView(this.iseditable);
    this.getBankDetail();
  }
}
