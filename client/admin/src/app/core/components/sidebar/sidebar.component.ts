import {
  Component,
  ElementRef,
  EventEmitter,
  OnInit,
  Output,
  Renderer2,
  ViewChild,
} from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { SidebarPaddingService } from 'src/app/shared/states/sidebar-padding.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
})
export class SidebarComponent implements OnInit {
  @ViewChild('sidebar') sidebar!: ElementRef;
  @ViewChild('vendorSubMenu') vendorSubMenu!: ElementRef;
  @ViewChild('vendorSubMenu2') vendorSubMenu2!: ElementRef;
  Message = 'Are you sure you want to log out?';
  iconSidebarClicked = false;
  showArrow = false;
  ngOnInit(): void {}

  constructor(
    private eventEmitter: EventEmitterService,
    private renderer: Renderer2,
    private authService: AuthService,
    private alertService: AlertService
  ) {}

  toggleSidebar() {
    const sidebarEl = this.sidebar.nativeElement;
    sidebarEl.classList.toggle('hidden');
  }
  toggleSubMenu() {
    const vendorSubMenu = this.vendorSubMenu.nativeElement;
    vendorSubMenu.classList.toggle('hidden');
  }
  // toggleSubMenu2() {
  //   const vendorSubMenu2 = this.vendorSubMenu2.nativeElement;
  //   vendorSubMenu2.classList.toggle('hidden');
  // }
  toggle(element: HTMLUListElement) {
    const elementRef = new ElementRef(element);
    const nativeElement = elementRef.nativeElement;
    nativeElement.classList.toggle('hidden');
  }
  iconSidebar() {
    this.iconSidebarClicked = true;
    const paddingValue = 44;
    this.eventEmitter.getsidebarEvent(paddingValue);
  }
  staticSidebar() {
    this.iconSidebarClicked = false;
    const paddingValue = 72;
    this.eventEmitter.getsidebarEvent(paddingValue);
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
  reloadWindow() {
    window.location.reload();
  }
}
