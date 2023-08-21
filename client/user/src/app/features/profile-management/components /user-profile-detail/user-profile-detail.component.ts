import { Component, NgZone, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { UserProfileService } from 'src/app/features/vendor/services/user-profile.service';
import { VendorProfileService } from 'src/app/features/vendor/services/vendor-profile.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { UserNameUpdteService } from 'src/app/shared/states/user-name-updte.service';
@Component({
  selector: 'app-user-profile-detail',
  templateUrl: './user-profile-detail.component.html',
  styleUrls: ['./user-profile-detail.component.css'],
})
export class UserProfileDetailComponent implements OnInit {
  formSubmitted = false;
  vendorDetailForm!: FormGroup;
  buttonDisabler = false;
  phoneNumberRegexp = new RegExp(/^[0-9]{10,15}$/);
  pincodeRegex = new RegExp(/^[0-9]{6,8}$/);
  userNameRegexp = new RegExp(/^[a-zA-Z0-9]{3,20}$/);
  iseditable: true | false = true;
  onlyNumberRegexp = new RegExp(/^[0-9]+$/);
  edithandleError = false;
  editButtonClicked = false;
  constructor(
    private eventEmitter: EventEmitterService,
    private profileService: UserProfileService,
    private toastService: ToastService,
    private _ngZone: NgZone,
    private route: Router,
    private ngxService: NgxUiLoaderService,
    private usernameService: UserNameUpdteService
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

    this.getUserDetail();
  }
  getUserDetail() {
    this.profileService.viewProfile().subscribe({
      next: (data: any) => {
        this.vendorDetailForm.patchValue({
          username: data.username,
          phone: data.phone,
          email: data.email,
        });
        localStorage.setItem('username', data.username);
        this.usernameService.updateUserName(data.username);
        console.log('in detail view=', localStorage.getItem('username'));
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
          this.getUserDetail();
          this.buttonDisabler = false;
          localStorage.setItem('username', data.username);

          this.usernameService.updateUserName(data.username);
          

        },
        error: () => {},
      });
    }
  }
  toggleEditOrView(iseditable: true | false): true | false {
    return this.iseditable === true ? false : true;
  }
  editHandle() {
    if (this.editButtonClicked) {
      this.vendorDetailForm.get('username')?.setErrors(null);
      this.vendorDetailForm.get('username')?.markAsUntouched();
      this.vendorDetailForm.get('username')?.markAsPristine();

      this.vendorDetailForm.get('phone')?.setErrors(null);
      this.vendorDetailForm.get('phone')?.markAsUntouched();
      this.vendorDetailForm.get('phone')?.markAsPristine();

      this.vendorDetailForm.reset();
      this.formSubmitted = false;
      this.getUserDetail();

      this.iseditable = this.toggleEditOrView(this.iseditable);
    } else {
      this.editButtonClicked = true;
      this.iseditable = this.toggleEditOrView(this.iseditable);
      this.getUserDetail();
    }
  }
}
