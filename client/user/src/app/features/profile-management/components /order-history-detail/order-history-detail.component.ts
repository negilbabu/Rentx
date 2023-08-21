import { Component, NgZone, OnInit } from '@angular/core';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { AddresListService } from '../../services/addres-service';
import { OrderService } from '../../services/order.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-order-history-detail',
  templateUrl: './order-history-detail.component.html',
  styleUrls: ['./order-history-detail.component.css']
})
export class OrderHistoryDetailComponent implements OnInit {

  totalAddress: any;
  showDropdown: any;
  items: any[] = [];
  closeFlag: any;
  isConfirm= false;
  orderId: any;
  orderData: any;

  constructor(private orderService: OrderService, private alertService:AlertService,
    private eventEmitter: EventEmitterService,
    private _ngZone: NgZone,
    private route: Router,
    public toast: ToastService,
    private router: ActivatedRoute) {
    }
   

  ngOnInit(): void {
this.getOrderData();
this.orderId=this.router.snapshot.params['id'];


  }
  getOrderData(){
    this.orderService.getOrderDetail(this.router.snapshot.params['id']).subscribe((res: any) => {
      this.orderData = res;
     
      console.log(res);

    });
  }

  requestHandle() {
    // this.isConfirm = true;
    this.alertService
      .openAlert('Do you want to cancel this order?')
      .subscribe((res) => {
        if (res) {
          this.cancelOrder();
        } else {
        
          return;
        }
      });
  }

cancelOrder(){
  this.orderService.cancelOrder(this.router.snapshot.params['id']).subscribe({
    next: (data: any) => {
      this.toast.showSucessToast('Order cancelled sucessfully', 'close');
      this._ngZone.run(() => {
        this.eventEmitter.onSaveEvent();

        this.route.navigate(['/profile/orderHistoryList']);
      });
    },
    error: (error:any) => {
      this.toast.showErrorToast(error.error.errorMessage, 'close');
      this._ngZone.run(() => {
        this.eventEmitter.onSaveEvent();
        // this.route.navigate(['/profile/listAddress']);
      });
    },
  });
}
}


