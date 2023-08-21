import { Component, NgZone, OnInit } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControl,
} from '@angular/forms';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { VendorProfileService } from 'src/app/features/vendor/services/vendor-profile.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';

@Component({
  selector: 'app-vendor-profile',
  templateUrl: './vendor-profile.component.html',
  styleUrls: ['./vendor-profile.component.css'],
})
export class VendorProfileComponent implements OnInit {
  formSubmitted = false;
  vendorDetailForm!: FormGroup;
  buttonDisabler = false;
  phoneNumberRegexp = new RegExp(/^[0-9]{10,15}$/);
  pincodeRegex = new RegExp(/^[0-9]{6,8}$/);
  userNameRegexp = new RegExp(/^[a-zA-Z0-9]{3,20}$/);
  iseditable: true | false = true;
  onlyNumberRegexp = new RegExp(/^[0-9]+$/);
  constructor(
    private eventEmitter: EventEmitterService,
    private profileService: VendorProfileService,
    private toastService: ToastService,
    private _ngZone: NgZone,
    private route: Router,
    private ngxService: NgxUiLoaderService
  ) {}
  ngOnInit(): void {
    this.vendorDetailForm = new FormGroup({
      username: new FormControl('', [
        Validators.required,
        Validators.maxLength(50),
        Validators.minLength(3),
        Validators.pattern(this.userNameRegexp),
      ]),
      phone: new FormControl('', [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(15),
        Validators.pattern(this.onlyNumberRegexp),
      ]),
      email: new FormControl({ value: '', disabled: true }),
    });

    this.getVendorDetail();
  }
  getVendorDetail() {
    this.profileService.viewProfile().subscribe({
      next: (data: any) => {
        this.vendorDetailForm.patchValue({
          username: data.username,
          phone: data.phone,
          email: data.email,
        });
        console.log(this.vendorDetailForm.value);
      },
      error: () => {},
    });
  }

  onProfileDetailsSubmit() {
    this.formSubmitted = true;
    if (this.vendorDetailForm.valid) {
      this.buttonDisabler = true;
      console.log(this.vendorDetailForm.value);
      this.profileService.updateProfile(this.vendorDetailForm.value).subscribe({
        next: (data: any) => {
          this.toastService.showSucessToast(
            'Profile Updated successfully',
            'close'
          );

          this.iseditable = this.toggleEditOrView(this.iseditable);

          this.getVendorDetail();
          this.buttonDisabler = false;
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

    this.getVendorDetail();
  }
}
