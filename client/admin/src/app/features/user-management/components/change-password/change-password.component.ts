import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { passwordMatch } from '../../validators/passwordMatchValidators';
import { ToastService } from 'src/app/shared/services/toast.service';
import { Router } from '@angular/router';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css'],
})
export class ChangePasswordComponent implements OnInit {
  formSubmitted = false;
  changePasswordForm!: FormGroup;
  wrongCurrentPasswordError = false;
  errorData:any;
  sameNewPasswordError = false;
  public showPassword: boolean = false;
  passwordregexp = new RegExp(
    /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?])(?!.*\s).{8,16}$/
  );
  public showNewPassword: boolean = false;
  samePasswordMsg: any;
  constructor(private authService: AuthService, private toast: ToastService,private route:Router,private errorHandlingService:ApiErrorHandlerService) {
    
  }

  ngOnInit(): void {
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
  );}
  onSubmit() {
    this.formSubmitted = true;

    if (this.changePasswordForm.valid) {
      const { confirmPassword, ...formData } = this.changePasswordForm.value;
      this.authService.changePassword(formData).subscribe({
        next: () => {
          this.toast.showSucessToast('Password changed successfully', 'close');
          this.route.navigateByUrl('/dashboard');
        },
        error: (error) => {
          if (this.errorData === '007') {
            this.route.navigateByUrl('/error');
          } else if (error.error.errorCode =="3005") {
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
}
