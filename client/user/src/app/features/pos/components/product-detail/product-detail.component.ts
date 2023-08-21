import { Component, ElementRef, NgZone, OnInit, ViewChild } from '@angular/core';
import { UserProductService } from '../../services/user-product.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CartStateService } from 'src/app/shared/states/cart-state.service';
import { CartServicesService } from 'src/app/features/cart/services/cart-services.service';
import { AuthService } from 'src/app/services/auth.service';
import { WishlistService } from 'src/app/features/profile-management/services/wishlist.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';

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
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css'],
})
export class ProductDetailComponent implements OnInit {

  @ViewChild('featureList') featureList!: ElementRef<HTMLElement>;
  @ViewChild('dateRangeInput') dateRangeInput!: ElementRef;
  maxDate!: Date;

  productDetail: any;
  similarProducts: any;
  productImageList: any;
  wishlistFlag=false;
  categoryId: any;
  wishListId:any;
  productId: any;
  numberOfDays: any;
  isAvailable: boolean = false;
  isLoaded: boolean = false;
  availabileQty!: any ;
  availableStock:any;
  availabilityMessageFlag:any;
  minDate: Date = new Date();
  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
    qty: new FormControl<string | null>('1', [
      Validators.required,
      Validators.pattern('^[0-9]+$'),
    ]),
  });
  startDate: any;
  endDate: any;
  currentCount: any;
  currentQuantity: any;
  errorData: any;

  constructor(
    private productService: UserProductService,
    private activatedRoute: ActivatedRoute,
    private cartStateService: CartStateService,
    private cartService: CartServicesService,
    private authService: AuthService,
    private router: Router,
    private wishlistService:WishlistService,
    private toastService:ToastService,
    private _ngZone: NgZone,
    private errorCodeHandle: ApiErrorHandlerService
  ) {
    const currentYear = new Date().getFullYear();
    this.maxDate = new Date(currentYear + 1, 11, 31);
    this.range.valueChanges.subscribe(() => {
      if (
        this.range.valid &&
        this.range.value.start != null &&
        this.range.value.end != null
      ) {
        console.log(this.range.value);
        this.checkAvailability(this.productId, this.range);
      }
    });
  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params) => {
      this.productId = params['pid'];

      this.productService.getProductById(this.productId).subscribe({
        next: (res) => {
          this.productDetail = res;
          this.productImageList = res.productImage;
          this.categoryId = res.categoryId;
          this.wishListId=res.wishlistId
          if(res.isWishlisted){
            this.wishlistFlag=true;
          }
          this.productImageList = Object.entries(this.productImageList[0]).map(
            ([key, value]) => ({ [key]: value })

          );
          console.log(this.productDetail, 'imgList');
          this.availableStock=res.availableStock;

        },
        error: (err) => {},
      });
    });
  }

  toggleElement(element: HTMLElement) {
    element.classList.toggle('hidden');
  }
  hasHiddenClass(element: HTMLElement): boolean {
    return element.classList.contains('hidden');
  }
  incrementQuantity() {
    const qtyControl = this.range.get('qty');
    let currentQty = 0;
    if (qtyControl?.value == '') {
      currentQty++;
      qtyControl.setValue(currentQty.toString());
      return;
    }
    if (qtyControl && typeof qtyControl.value === 'string') {
      let currentQty = parseInt(qtyControl.value, 10);
      if (currentQty >= 999999) {
        return;
      }

      currentQty++;
      qtyControl.setValue(currentQty.toString());
      this.checkAvailability(this.productId, this.range);
      console.log(qtyControl.value, 'qty value');
    }
  }
  decrementQuantity() {
    const qtyControl = this.range.get('qty');
    if (qtyControl?.value == '') {
      return;
    }
    if (qtyControl && typeof qtyControl.value === 'string') {
      let currentQty = parseInt(qtyControl.value, 10);
      if (currentQty <= 1) {
        return;
      }
      currentQty--;
      qtyControl.setValue(currentQty.toString());
      console.log(qtyControl.value, 'qty value');
      this.checkAvailability(this.productId, this.range);
    }
  }
  onQuantityEnter(){
   
    this.checkAvailability(this.productId,this.range)
  }

  checkAvailability(productId: any, range: any) {
    console.log('Availability');
    
    this.productService
      .checkProductAvailability(this.productId, this.range.value)
      .subscribe({
        next: (res) => {
          console.log('yeaha',res);

          console.log('prrrrr',this.range.value)
          this.isAvailable = res.isAvailable;
          // alert(this.isAvailable)
          this.currentQuantity=this.range.value.qty;
          this.availabileQty = res.remainingCount;
          if( this.currentQuantity>this. availableStock  ){
            this.availabilityMessageFlag=1
          }
          else{
            this.availabilityMessageFlag=0
          }
        },
        error: (err) => {},
      });
  }

  handleAddToWishList(id: any) {
    this.wishlistService.addToWishlist(id).subscribe({
      next: (res) => {
        this._ngZone.run(() => {
          this.wishListId=res;
            this.wishlistFlag=true;
        this.toastService.showSucessToast('Added to your wishlist', 'close');
          });  },error: (error: any) => {
            
               this._ngZone.run(() => {
             
            this.errorData = this.errorCodeHandle.getErrorMessage(
              error.error.errorCode
            );
            if (this.errorData === '007') {
              this.router.navigateByUrl('/error');
            } else {
              this.toastService.showErrorToast(this.errorData, 'close');
            }
            })
          }
    })
  }
  handleRemoveFromWishList(id: any) {
    this.wishlistService.removeFromWishlist(id).subscribe({
      next: (res) => {
        this._ngZone.run(() => {
         
          this.wishlistFlag=false;
        this.toastService.showDeleteToast('Removed from your wishlist', 'close');
    
          });  },error: (error: any) => {
               this._ngZone.run(() => {
            this.errorData = this.errorCodeHandle.getErrorMessage(
              error.error.errorCode
            );
            if (this.errorData === '007') {
              this.router.navigateByUrl('/error');
            } else {
              this.toastService.showErrorToast(this.errorData, 'close');
            }
            })
          }
    })
  }

  dateFormatted(date: any) {
    if (date == null) {
      return null;
    }
    let formattedDate = new Date(date);
    formattedDate.setDate(formattedDate.getDate() + 1); // Add one more day
    return formattedDate.toISOString().substring(0, 10);
  }

  handleAddToCart(product: any) {
    if (this.authService.isAuthenticated()) {
      let qty = this.range?.get('qty')?.value;
      this.startDate = this.dateFormatted(this.range.value.start);
      this.endDate = this.dateFormatted(this.range.value.end);
      this.cartService
        .addToCart(
          product.id,
          qty,
          this.startDate ? this.startDate : undefined,
          this.endDate ? this.endDate : undefined
        )
        .subscribe({
          next: (res) => {
            this.currentCount = res.cartCount;
            localStorage.setItem('cartCount', this.currentCount.toString());
            this.cartStateService.updateCartCount(this.currentCount);
            this.router.navigate(['/cart']);
          },
        });
    } else {
      this.router.navigate(['/login']);
    }
  }

  handleGoToCart() {
    this.router.navigate(['/cart']);
  }

  transformToTitleCase(value: any): string {
    return value.charAt(0).toLocaleUpperCase() + value.slice(1);
  }
}
