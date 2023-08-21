import { Component, NgZone, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Route, Router } from '@angular/router';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { CartStateService } from 'src/app/shared/states/cart-state.service';
import { WishlistService } from '../../services/wishlist.service';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-order-history-list',
  templateUrl: './order-history-list.component.html',
  styleUrls: ['./order-history-list.component.css']
})
export class OrderHistoryListComponent implements OnInit {
  cartData: any;
  totalPrice: any;
  currentCount: any;
  availableProducts: any;
  errorData: any;

  constructor(private route:Router,
    private _ngZone: NgZone,
    private eventEmitter:EventEmitterService 
    ,public toast: ToastService,

    private orderService: OrderService, 
    private cartStateService: CartStateService, 

  
    private formBuilder:FormBuilder,

    private alertService: AlertService,private errorCodeHandle: ApiErrorHandlerService){}

  ngOnInit(): void {
    this.getOrderData();
  }
  goToDetails(product: any) {
    let queryParams: {
      pid?: number;
    } = {};

    queryParams.pid = product.id;
    this.route.navigate(['/detail', product.name], { queryParams });
  }
  
  isDateRangeInvalid(product: any): boolean {
    return !product.startDate || !product.endDate;
  }

  getOrderData(){
    this.orderService.getOrderlist().subscribe((res: any) => {
      console.log("lee", res);
      this.cartData = res.result;
      this.totalPrice = res.totalPrice;
      this.currentCount=res.result.length;
      console.log(this.cartData);
      this.availableProducts = this.cartData.filter((product: { isAvailable: boolean }) => product.isAvailable === true);

    });
  }


  goToProducts() {
    this.route.navigate(['/allProducts']);
  }



  getImageUrl(image: any, index: number): string {
    const imageKey = 'image' + (index + 1);
    return image[imageKey];
  }



  


    
  
    }