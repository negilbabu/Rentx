import { Component, ElementRef, OnInit, ViewChild, AfterViewInit, NgZone } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { CartStateService } from 'src/app/shared/states/cart-state.service';
import { CartServicesService } from 'src/app/features/cart/services/cart-services.service';
import flatpickr from 'flatpickr';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { Route, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { AlertService } from 'src/app/shared/services/alert.service';
import { Subject, debounceTime } from 'rxjs';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import { cartToCheckoutService } from 'src/app/shared/states/cartToCheckout.service';
import { WishlistService } from '../../services/wishlist.service';


@Component({
  selector: 'app-wishlist-view',
  templateUrl: './wishlist-view.component.html',
  styleUrls: ['./wishlist-view.component.css']
})
export class WishlistViewComponent implements OnInit {
  @ViewChild('dateRangeInput') dateRangeInput!: ElementRef;
  currentCount:any;
  tempData: any = [];
  cartData: any;
  minDate: Date = new Date();
  maxDate!: Date;
  roductId: any;
  numberOfDays: any;
  isAvailable: boolean = false;
  isLoaded: boolean = false;
  cartFormArray: FormGroup[] = [];
  startDate:any;
  endDate:any;
  quantity:any;
  cartId:any;
  availableProducts:any;
  private editCartSubject = new Subject();


  cartEditForm: FormGroup[] = [];
  totalPrice: any;
  flatpickrInstance: flatpickr.Instance | null = null;
  errorData: any;
  quantitySubject: any;
  startDateValidator: any=false;
  endDateValidator: any=false;

  constructor(private route:Router,
    private _ngZone: NgZone,
    private eventEmitter:EventEmitterService 
    ,public toast: ToastService,
    private wishlistService: WishlistService, 
    private cartStateService: CartStateService, 
    private cartToCheckoutService: cartToCheckoutService, 
  
    private formBuilder:FormBuilder,

    private alertService: AlertService,private errorCodeHandle: ApiErrorHandlerService
    
   ) { }

  ngOnInit(): void {
    this.getWishlistData();
  }
  goToDetails(product: any) {
    
this.route.navigateByUrl('/detail/'+product.productName+'?pid='+product.productId)
  }
  
  isDateRangeInvalid(product: any): boolean {
    return !product.startDate || !product.endDate;
  }

  getWishlistData(){
    this.wishlistService.getWishlist().subscribe((res: any) => {
      console.log("lee", res);
      this.cartData = res.result;
      this.totalPrice = res.totalPrice;
      this.currentCount=res.result.length;
      console.log(this.cartData);
      this.availableProducts = this.cartData.filter((product: { isAvailable: boolean }) => product.isAvailable === true);

    });
  }

  requestHandle(product: any) {
    // this.isConfirm = true;
    this.alertService
      .openAlert('Remove the product from wishlist?')
      .subscribe((res) => {
        if (res) {
          this.removeFromWishlist(product);
        } else {
          // this.isConfirm = false;
          return;
        }
      });
  }

  goToProducts() {
    this.route.navigate(['/allProducts']);
  }



  getImageUrl(image: any, index: number): string {
    const imageKey = 'image' + (index + 1);
    return image[imageKey];
  }



  


  removeFromWishlist(product: any) {
    this.wishlistService.removeFromWishlist(product.id).subscribe({
      next: (res:any) => {

        this._ngZone.run(() => {
        this.eventEmitter.onDeleteEvent();
        this.toast.showDeleteToast('Removed from wishlist', 'close');
        // this.route.navigateByUrl('/cart');
//remove count in  nav bar
        this.ngOnInit();
          });  },error: (error: any) => {
            this.errorData = this.errorCodeHandle.getErrorMessage(
              error.error.errorCode
            );
            if (this.errorData === '007') {
              this.route.navigateByUrl('/error');
            } else {
              this.toast.showErrorToast(this.errorData, 'close');
            }
          },
        });
      }
    
  
  

  
  
      








}


