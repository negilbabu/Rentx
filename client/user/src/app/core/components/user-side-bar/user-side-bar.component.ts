import {
  Component,
  ElementRef,
  EventEmitter,
  OnDestroy,
  OnInit,
  Output,
  Renderer2,
  ViewChild,
} from '@angular/core';
import { Subscription } from 'rxjs';
import { UserProfileService } from 'src/app/features/vendor/services/user-profile.service';
import { VendorProfileService } from 'src/app/features/vendor/services/vendor-profile.service';
import { AuthService } from 'src/app/services/auth.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { UserNameUpdteService } from 'src/app/shared/states/user-name-updte.service';

@Component({
  selector: 'app-user-side-bar',
  templateUrl: './user-side-bar.component.html',
  styleUrls: ['./user-side-bar.component.css'],
})
export class UserSideBarComponent implements OnInit {
  private sidebarPaddingSubscription!: Subscription;
  @ViewChild('sidebar') sidebar!: ElementRef;
  Message = 'Are you sure you want to log out?';
  type: any;
  collapsed = false;
  showArrow = false;
  iconSidebarClicked = false;
  username: any;
  actualUsername: any;

  ngOnInit(): void {
    this.getUserDetail();
    console.log("lname from locl storage=",localStorage.getItem('username'));
   
    this.getUserType();
    localStorage.setItem('pl', '70');
    this.userNameService.userNameUpdate$.subscribe((user) => {
      this.username = user;
    
    });
    
  }
  getUserDetail() {
    this.profileService.viewProfile().subscribe({
      next: (data: any) => {
       
     localStorage.setItem('username', data.username);
     console.log("unamz=", data.username);
    console.log("lname from locl storage=",localStorage.getItem('username'));

     this.username=data.username;
     this.userNameService.updateUserName(data.username);
      this.truncateUsername(this.username);

      },
      
      error: () => {},
    });
  }
  truncateUsername(username: any, maxLength: number = 10): string {
    console.log('method', username);

    if (username.length <= maxLength) {
      console.log(' if -', username);

      return (this.username = username);
    } else {
      console.log(' el if -', username);

      this.actualUsername = username;
      return (this.username = username.slice(0, maxLength) + '...');
    }
  }

  constructor(
    private profileService: UserProfileService,
    private eventEmitter: EventEmitterService,
    private renderer: Renderer2,
    private authService: AuthService,
    private alertService: AlertService,
    private userNameService:UserNameUpdteService
  ) {}
  ngOnDestroy(): void {
    if (this.sidebarPaddingSubscription) {
      this.sidebarPaddingSubscription.unsubscribe();
    }
  }

  toggleSidebar() {
    const sidebarEl = this.sidebar.nativeElement;
    sidebarEl.classList.toggle('hidden');
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
  logOut() {
    this.alertService.openAlert(this.Message).subscribe((res) => {
      if (res) {
        this.authService.logOut();
        this.ngOnInit();
      } else {
        return;
      }
    });
  }
  toggleSidebar2() {
    this.collapsed = !this.collapsed;
  }
  iconSidebar() {
    this.iconSidebarClicked = true;
    const paddingValue = 36;
    this.eventEmitter.getsidebarEvent(paddingValue);
  }
  staticSidebar() {
    this.iconSidebarClicked = false;
    const paddingValue = 72;
    this.eventEmitter.getsidebarEvent(paddingValue);
  }

  reloadWindow() {
    window.location.reload();
  }
}
