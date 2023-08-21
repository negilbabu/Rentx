import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { passwordMatch } from 'src/app/features/user-management/validators/passwordMatchValidators';
import { UserProfileService } from 'src/app/features/vendor/services/user-profile.service';
import { VendorProfileService } from 'src/app/features/vendor/services/vendor-profile.service';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import { AuthService } from 'src/app/services/auth.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';

@Component({
  selector: 'app-change-user-password',
  templateUrl: './change-user-password.component.html',
  styleUrls: ['./change-user-password.component.css']
})
export class ChangeUserPasswordComponent implements OnInit {

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
  samePasswordMsg:any;
  constructor(private eventEmitterService:EventEmitterService,private profileService:UserProfileService,
    private authService: AuthService,
    private toast: ToastService,
    private route: Router,
    private errorHandlingService: ApiErrorHandlerService
  ) {}

  ngOnInit(): void {
    this.getUserType();
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
  getUserType() {
    
    this.profileService.viewProfile().subscribe({
      next: (data: any) => {
        localStorage.setItem('type', data.type.toString());
        this.type = localStorage.getItem('type');
        },
      error: () => {},
    });
  }
  onSubmit() {
    this.formSubmitted = true;

    if (this.changePasswordForm.valid) {
      const { confirmPassword, ...formData } = this.changePasswordForm.value;
      this.authService.changeUserPassword(formData).subscribe({
        next: () => {
          this.toast.showSucessToast('Password changed successfully', 'close');
          this.route.navigateByUrl('/home');
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
    this.authService.addUserPassword(formData).subscribe({
      next: () => {
        localStorage.setItem('type','0'); 
        this.eventEmitterService.reloadSidebarEvent();
        this.route.navigateByUrl('/home');
        this.toast.showSucessToast('Password added successfully', 'close');
        
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
}
}
