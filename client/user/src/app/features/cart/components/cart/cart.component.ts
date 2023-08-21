import { Component, ElementRef, OnInit, ViewChild, AfterViewInit, NgZone } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { CartStateService } from 'src/app/shared/states/cart-state.service';
import { CartServicesService } from '../../services/cart-services.service';
import flatpickr from 'flatpickr';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { Route, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { AlertService } from 'src/app/shared/services/alert.service';
import { Subject, debounceTime } from 'rxjs';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import { cartToCheckoutService } from 'src/app/shared/states/cartToCheckout.service';



// Allow only numbers and certain special keys
(window as any).onlyNumbers = function (event: any) {
  const key = event.key;
  if (!/^[0-9\s\b]+$/.test(key) && !isSpecialKey(event)) {
    event.preventDefault();
    return false;
  }
  return true;
};
function isSpecialKey(event: any) {
  const specialKeys = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight', 'Tab'];
  return specialKeys.includes(event.key);
}
@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})

export class CartComponent implements OnInit {
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
    private cartStateService: CartStateService, 
    private cartToCheckoutService: cartToCheckoutService, 
    private cartService: CartServicesService,
    private formBuilder:FormBuilder,

    private alertService: AlertService,private errorCodeHandle: ApiErrorHandlerService
    
   ) { this.editCartSubject.pipe(debounceTime(800)).subscribe(() => {
    this.editCart();
  });}

  ngOnInit(): void {
    this.getCartData();
    this.cartData.forEach((product:any) => {
      const formGroup = this.formBuilder.group({
        quantity: [product.quantity] // Set initial value for quantity
      });
  
      this.cartFormArray.push(formGroup); // Add form group to the array
    });
    this.quantitySubject.pipe(debounceTime(500)).subscribe((quantity:any) => {
  
      this.addQuantity(quantity);
    });

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

  getCartData(){
    this.cartService.getCartData().subscribe((res: any) => {
      console.log("lee", res);
      this.cartData = res.cartProducts.result;
      this.totalPrice = res.totalPrice;
      this.currentCount=res.cartProducts.numItems;
      console.log(this.cartData);
      this.availableProducts = this.cartData.filter((product: { isAvailable: boolean }) => product.isAvailable === true);

      localStorage.setItem('cartCount', this.currentCount.toString());
    this.cartStateService.updateCartCount(this.currentCount);
    });
  }

  requestHandle(product: any) {
    // this.isConfirm = true;
    this.alertService
      .openAlert('Do you want to remove the product?')
      .subscribe((res) => {
        if (res) {
          this.removeFromCart(product);
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

  addQuantity(cart:any) {
    
    this.quantity=cart.quantity+1;
    console.log(this.quantity+1);
    this.cartId=cart.id;
    this.startDate=cart.startDate;
    this.endDate=cart.endDate;
    this.editCart();

  }

  minusQuantity(cart:any) {
  this.quantity=cart.quantity-1;
  console.log(this.quantity+1);
  this.cartId=cart.id;
  this.startDate=cart.startDate;
  this.endDate=cart.endDate;
  this.editCart();
}
quantityEdit(event: any, cart: any) {
  this.quantity = event.target.value;
  this.cartId = cart.id;
  this.startDate = cart.startDate;
  this.endDate = cart.endDate;
  
  // Apply debounceTime operator
  this.editCartSubject.next(undefined);
}


  removeFromCart(product: any) {
    this.cartService.removeProduct(product.id).subscribe({
      next: (res:any) => {

        this._ngZone.run(() => {
        this.eventEmitter.onDeleteEvent();
        this.route.navigateByUrl('/cart');
//remove count in  nav bar
        if (this.currentCount > 0) {
          this.currentCount--;
          localStorage.setItem('cartCount', this.currentCount.toString());
          this.cartStateService.updateCartCount(this.currentCount);
        }
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
    
  
  

  
      editStartDate(startDate: any, cart: any) {
        this.startDateValidator=startDate.target.value;
        this.cartId = cart.id;
        this.quantity = cart.quantity;
        const date = new Date(startDate.target.value);
        date.setDate(date.getDate() + 1); // Add one more day
        const formattedDate = date.toISOString().substring(0, 10);
        this.startDate = formattedDate;
      }
      
editEndDate(endDate: any, product: any) {
  this.endDateValidator=true;
  this.quantity = product.quantity;
  const date = new Date(endDate.target.value);
  date.setDate(date.getDate() + 1); // Add one more day
  const formattedDate = date.toISOString().substring(0, 10);
  this.endDate = formattedDate;
  this.editDates()
}
editDates(){
  // alert(this.endDate+"yehaa")
  if ( this.endDate!='1970-01-02'){  
    console.log("end",this.endDate)
  console.log("start",this.startDate)

  this.cartService.editProductFromCart(this.cartId,this.quantity,this.startDate,this.endDate).subscribe({
    next: (res) => {
      this._ngZone.run(() => {
        this.eventEmitter.onSaveEvent();
        this.toast.showSucessToast('Rent Dates Updated', 'close');
        // this.route.navigateByUrl('/cart');
        this.ngOnInit();
          });},
    
    error: (error: any) => {
      this.errorData = error.error.errorMessage;
      this.errorData =
        this.errorData.charAt(0).toUpperCase() +
        this.errorData.slice(1).toLowerCase();
      // this.toast.showErrorToast(this.errorData, 'close');
    },
  })

}
else{
  this.eventEmitter.onSaveEvent();
}
}

editCart(){
  if(this.quantity!=0){
  this.cartService.editProductFromCart(this.cartId,this.quantity,this.startDate,this.endDate).subscribe({
    next: (res) => {
      this._ngZone.run(() => {
      this.toast.showSucessToast('Quantity Updated', 'close');
      this.getCartData();
        });  },error: (error: any) => {
             this._ngZone.run(() => {
          this.errorData = this.errorCodeHandle.getErrorMessage(
            error.error.errorCode
          );
          if (this.errorData === '007') {
            this.route.navigateByUrl('/error');
          } else {
            this.toast.showErrorToast(this.errorData, 'close');
            this.getCartData();
          }

           })
        },
      });
}else{
  this.toast.showErrorToast('Invalid Quantity', 'close');
  this.getCartData();
}

}

checkOut() {
 
  const idList = this.availableProducts.map((product: { id: number }) => product.id);

  this.cartToCheckoutService.cartToCheckout(idList);
  this.route.navigate(['/order/checkout']);
}

CheckoutHandle() {
  // this.isConfirm = true;
  this.alertService
    .openAlert('Checkout with available products?')
    .subscribe((res) => {
      if (res) {
        this.checkOut();
      } else {
        // this.isConfirm = false;
        return;
      }
    });
}



}

