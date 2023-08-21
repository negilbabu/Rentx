import { Component, NgZone, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { OrderService } from 'src/app/features/profile-management/services/order.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { VendorOrderService } from '../../../services/vendor-order.service';

@Component({
  selector: 'app-vendor-order-detail-view',
  templateUrl: './vendor-order-detail-view.component.html',
  styleUrls: ['./vendor-order-detail-view.component.css']
})
export class VendorOrderDetailViewComponent implements OnInit {

  totalAddress: any;
  showDropdown: any;
  items: any[] = [];
  closeFlag: any;
  isConfirm= false;
  orderId: any;
  orderData: any;

  constructor(private orderService: VendorOrderService, private alertService:AlertService,
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
     console.log(res)
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
        console.log("inside cancel order")
        this.route.navigate(['/vendor/VendorOrderList']);
      });
    },
    error: (error:any) => {
      this.toast.showErrorToast(error.error.errorMessage, 'close');
      this._ngZone.run(() => {
        this.eventEmitter.onSaveEvent();
        this.route.navigate(['/vendor/VendorOrderList']);
      });
    },
  });
}
}


