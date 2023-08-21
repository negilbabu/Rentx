import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { EventEmitterService } from 'src/app/services/event-emitter.service';


@Component({
  selector: 'app-vendor-home',
  templateUrl: './vendor-home.component.html',
  styleUrls: ['./vendor-home.component.css'],
})
export class VendorHomeComponent implements OnInit, OnDestroy {
  private paddingSubscription!: Subscription;

  cartCount: any;

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
