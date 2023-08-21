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
import { VendorProfileService } from 'src/app/features/vendor/services/vendor-profile.service';
import { AuthService } from 'src/app/services/auth.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { AlertService } from 'src/app/shared/services/alert.service';


@Component({
  selector: 'app-vendor-sidebar',
  templateUrl: './vendor-sidebar.component.html',
  styleUrls: ['./vendor-sidebar.component.css'],
})
export class VendorSidebarComponent implements OnInit, OnDestroy {
  private sidebarPaddingSubscription!: Subscription;
  @ViewChild('sidebar') sidebar!: ElementRef;
  Message = 'Are you sure you want to log out?';
  collapsed = false;
  showArrow = false;
  iconSidebarClicked = false;
  type: any;
  eventSubscription!: Subscription;
  ngOnInit(): void {
    localStorage.setItem('pl', '70');
    this.getUserType();
    this.eventSubscription = this.eventEmitter
      .onReloadSidebar()
      .subscribe(() => {
       
        this.getUserType();
      });
  }

  constructor(
    private profileService: VendorProfileService,
    private eventEmitter: EventEmitterService,
    private renderer: Renderer2,
    private authService: AuthService,
    private alertService: AlertService,

  ) {}
  ngOnDestroy(): void {
    if (this.sidebarPaddingSubscription) {
      this.sidebarPaddingSubscription.unsubscribe();
    }
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
  toggleSidebar() {
    const sidebarEl = this.sidebar.nativeElement;
    sidebarEl.classList.toggle('hidden');
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
  toggle(element: HTMLUListElement) {
    const elementRef = new ElementRef(element);
    const nativeElement = elementRef.nativeElement;
    nativeElement.classList.toggle('hidden');
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
