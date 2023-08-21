import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { EventEmitterService } from 'src/app/services/event-emitter.service';

@Component({
  selector: 'app-user-home',
  templateUrl: './user-home.component.html',
  styleUrls: ['./user-home.component.css']
})
export class UserHomeComponent implements OnInit {
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
