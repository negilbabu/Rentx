import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { passwordMatch } from 'src/app/features/user-management/validators/passwordMatchValidators';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import { AuthService } from 'src/app/services/auth.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';

@Component({
  selector: 'app-vendor-change-password',
  templateUrl: './vendor-change-password.component.html',
  styleUrls: ['./vendor-change-password.component.css'],
})
export class VendorChangePasswordComponent implements OnInit {
  formSubmitted = false;
  changePasswordForm!: FormGroup;
  addPasswordForm!: FormGroup;
  public showPassword: boolean = false;
  public showNewPassword: boolean = false;
  passwordregexp = new RegExp(
    /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]).{8,16}$/
  );
  errorData: any;
  type: any;
  wrongCurrentPasswordError = false;
  sameNewPasswordError = false;
  samePasswordMsg: any;
  constructor(private eventEmitterService:EventEmitterService,
    private authService: AuthService,
    private toast: ToastService,
    private route: Router,
    private errorHandlingService: ApiErrorHandlerService
  ) {}

  ngOnInit(): void {
    this.type = localStorage.getItem('type');
    this.changePasswordForm = new FormGroup(
      {
        oldPassword: new FormControl('', [Validators.required]),
        newPassword: new FormControl('', [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(this.passwordregexp),
          Validators.maxLength(16),
        ]),
        confirmPassword: new FormControl('', [Validators.required]),
      },
      [passwordMatch('newPassword', 'confirmPassword')]
    );
    this.addPasswordForm = new FormGroup(
      {
       
        newPassword: new FormControl('', [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(this.passwordregexp),
          Validators.maxLength(16),
        ]),
        confirmPassword: new FormControl('', [Validators.required]),
      },
      [passwordMatch('newPassword', 'confirmPassword')]
    );
  }
  onSubmit() {
    this.formSubmitted = true;

    if (this.changePasswordForm.valid) {
      const { confirmPassword, ...formData } = this.changePasswordForm.value;
      this.authService.changePassword(formData).subscribe({
        next: () => {
          this.toast.showSucessToast('Password changed successfully', 'close');
          this.route.navigateByUrl('/vendor/dashboard');
        },
        error: (error) => {
          if (this.errorData === '007') {
            this.route.navigateByUrl('/error');
          } else if (error.error.errorCode == 3005) {
            this.samePasswordMsg = "New Password must differ"
            this.sameNewPasswordError = true;
          } else {
         
            this.wrongCurrentPasswordError = true;
            this.errorData = this.errorHandlingService.getErrorMessage(
              error.error.errorCode
            );
          }
         
         
        }
      });
    } else {
    }
  }
  handleOldPasswordChange() {
    this.wrongCurrentPasswordError = false;
  }

  handleNewPasswordChange() {
    this.sameNewPasswordError = false;
  }

//Add password
onAddPasswordSubmit() {
  this.formSubmitted = true;

  if (this.addPasswordForm.valid) {
    const { confirmPassword, ...formData } = this.addPasswordForm.value;
    this.authService.addPassword(formData).subscribe({
      next: () => {
        localStorage.setItem('type','0'); 
        this.eventEmitterService.reloadSidebarEvent();
        this.toast.showSucessToast('Password added successfully', 'close');
        this.route.navigateByUrl('/vendor/dashboard');
         // Notify the sidebar component
      },
      error: (error) => {
        this.errorData = this.errorHandlingService.getErrorMessage(
          error.error.errorCode
        );
        if (this.errorData === '007') {
          this.route.navigateByUrl('/error');
        } else {
          this.toast.showErrorToast(this.errorData, 'close');
        } 
        
       
        
        
      }
    });
    
  } else {
  }
}}