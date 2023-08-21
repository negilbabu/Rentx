import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { SidebarPaddingService } from 'src/app/shared/states/sidebar-padding.service';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css'],
})
export class AdminHomeComponent implements OnInit, OnDestroy {
  eventSubscription!: Subscription;
  paddingValue: any = 72;
  constructor(private eventEmitter: EventEmitterService) {}
  ngOnDestroy(): void {
    this.eventSubscription.unsubscribe();
  }
  ngOnInit() {
    this.eventSubscription = this.eventEmitter
      .sidebarEvent$()
      .subscribe((padding) => {
        this.paddingValue = padding;
      });
  }

  
}
