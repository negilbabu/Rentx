import { Component, NgZone, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  MatSnackBar,
  MatSnackBarHorizontalPosition,
  MatSnackBarVerticalPosition,
} from '@angular/material/snack-bar';
import { Router } from '@angular/router';

import { UserService } from 'src/app/features/user-management/services/user.service';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import { AuthService } from 'src/app/services/auth.service';
import { ToastService } from 'src/app/shared/services/toast.service';

@Component({
  selector: 'app-otp-verify',
  templateUrl: './otp-verify.component.html',
  styleUrls: ['./otp-verify.component.css'],
})
export class OtpVerifyComponent implements OnInit {
  otpNumberRegexp = new RegExp(/^[0-9]{1,6}$/);
  formSubmitted = false;
  otpForm!: FormGroup;
  errorData: any;
  remainingTime =120;
  intervalId: any;
  timerEnded = false;
  isResendClicked=false;
  constructor(
    public toast: ToastService,
    private userService: UserService,
    private router: Router,
    private _ngZone: NgZone,
    private authService: AuthService,private errorCodeHandle: ApiErrorHandlerService
  ) {}

  ngOnInit(): void {
    this.otpForm = new FormGroup({
      otp: new FormControl('', [
        Validators.required,
        Validators.pattern(this.otpNumberRegexp),
      ]),
    });


    // resend otp timer starts
    if(localStorage.getItem('timerEnded')=='true'){
      this.timerEnded=true;
    }else{

      this.startTimer();
    }
  }

  startTimer() {
    const storedTime = localStorage.getItem('remainingTime');

    if (storedTime) {
      this.remainingTime = parseInt(storedTime, 10);
    }
    this.intervalId = setInterval(() => {
      this.tick();
    }, 1000);
  }

  tick() {
    this.remainingTime--;
    localStorage.setItem('remainingTime', this.remainingTime.toString());

    if (this.remainingTime === 0) {
      clearInterval(this.intervalId);
      localStorage.removeItem('remainingTime');
      this.timerEnded = true;
      localStorage.setItem('timerEnded',this.timerEnded.toString())
      this.isResendClicked=false;
    }
  }

  ngOnDestroy() {
    this.stopTimer();
    localStorage.removeItem('remainingTime');
  }

  stopTimer() {
    this.isResendClicked=false;
    clearInterval(this.intervalId);
  }
  onResendOtp() {
    this.isResendClicked=true;
    this.authService.resendOtp().subscribe({
      next:()=>{
        this.timerEnded = false;
        this.remainingTime=120;
        this.startTimer()
        
      },error:(error)=>{
        this.errorData = this.errorCodeHandle.getErrorMessage(
          error.error.errorCode
        );
        if (this.errorData === '007') {
          this.router.navigateByUrl('/error');
        } else {
          this.toast.showErrorToast(this.errorData, 'close');
        }
        this.router.navigateByUrl('/register')
      }
    });
  }

  onSubmit() {
    this.formSubmitted = true;
    if (this.otpForm.valid) {
      this.userService.verifyOtp(this.otpForm.value.otp).subscribe({
        next: () => {
          this._ngZone.run(() => {
            this.toast.showSucessToast(
              'User Registered Successfully!',
              'close'
            );
            localStorage.clear();
            this.router.navigate(['/login']);
          });
        },
        error: (error: any) => {
          this.errorData = this.errorCodeHandle.getErrorMessage(
            error.error.errorCode
          );
          if (this.errorData === '007') {
            this.router.navigateByUrl('/error');
          } else {
            this.toast.showErrorToast(this.errorData, 'close');
          }
        },
      });
    }
  }
}
