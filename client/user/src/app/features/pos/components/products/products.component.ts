import { Component, Input, NgZone, OnInit } from '@angular/core';
import { UserProductService } from '../../services/user-product.service';
import { ActivatedRoute, Router } from '@angular/router';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { CartServicesService } from 'src/app/features/cart/services/cart-services.service';
import { CartStateService } from 'src/app/shared/states/cart-state.service';
import { AuthService } from 'src/app/services/auth.service';
import { Pipe, PipeTransform } from '@angular/core';
import { Subscription } from 'rxjs';
import { WishlistService } from 'src/app/features/profile-management/services/wishlist.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css'],
})
export class ProductsComponent implements OnInit {
  categoryFilterParams: string[] = [];
  StoreFilterParams: string[] = [];
  searchParam!: string;
  pageNum!: number;
  sortParam!: string;
  sortOrder!: string;
  selectedCategoryFilter: string[] = [];
  selectedStoreFilter: string[] = [];
  productList: any[] = [];
  isLoading = true;
  @Input() dateRange!: any;
  currentCount: any;
  startDate: any;
  endDate: any;
  eventSubscription!: Subscription;
  startDateParam: any;
  endDateParam: any;
  errorData: any;

  @Input() latitudeParam!:any;
  @Input() longitudeParam!:any;
  
  
  constructor(
    private cartService: CartServicesService,
    private productService: UserProductService,
    private activatedRoute: ActivatedRoute,
    private eventEmitter: EventEmitterService,
    private router: Router,
    private cartStateService: CartStateService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private wishlistService:WishlistService,
    private toastService:ToastService,
    private _ngZone: NgZone,
    private errorCodeHandle: ApiErrorHandlerService
  ) {}

  ngOnInit(): void {
    
    this.eventSubscription = this.eventEmitter
    .onFilterDates()
    .subscribe((filterDates) => {
      this.startDate = filterDates.startDate;
      this.endDate = filterDates.endDate;
      console.log("Start = " +this.startDate)
      console.log("End = " +this.endDate)
    this.loadData();
    });
    this.activatedRoute.queryParams.subscribe((params) => {
    this.startDateParam=params['startDatex  '];
    this.endDateParam=params['endDate'];
    if(params['latitude']!=undefined){

      this.latitudeParam=params['latitude']
      this.longitudeParam=params['longitude']
      console.log(this.latitudeParam,"loginfinside product list");
    }
    // this.latitudeParam=params['latitude'];
    // this.longitudeParam=params['longitude']
      this.searchParam = params['search'];
      this.pageNum = params['page'];
      this.sortParam = params['sortBy'];
      this.sortOrder = params['sortIn'];
      this.categoryFilterParams = (
        params['categoryFilterParams']
          ? params['categoryFilterParams'].split('_')
          : []
      )
        .map((item: any) => parseInt(item.split(':')[1]))
        .join(',');

      this.StoreFilterParams = (
        params['storeFilterParams']
          ? params['storeFilterParams'].split('_')
          : []
      )
        .map((item: any) => parseInt(item.split(':')[1]))
        .join(',');
       
 
      // Call API with the received parameters
      this.loadData();
    });
  }

  loadData() {
    
    this.isLoading = true;
    if (!this.startDate) {
      this.startDate = new Date().toISOString().split('T')[0];
    }
    
    if (!this.endDate) {
      this.endDate = new Date().toISOString().split('T')[0];
    }
    
    this.productService
      .listProduct(
        this.pageNum,
        undefined,
        this.sortParam,
        this.sortOrder,
        this.searchParam,
        this.categoryFilterParams.toString(),
        this.StoreFilterParams.toString(),
        this.startDate,
        this.endDate,
        this.latitudeParam,
        this.longitudeParam,

      )
      .subscribe((res) => {
        this.productList = res.result;
        console.log(res, 'plist');

        this.eventEmitter.onLoadEvent();
        setTimeout(() => {
          this.isLoading = false;
        }, 4000);
        console.log(this.productList, ':productList');
      });
  }

  goToDetails(product: any) {
    let queryParams: {
      pid?: number;
    } = {};

    queryParams.pid = product.id;
    this.router.navigate(['/detail', product.name], { queryParams });
  }
  
  dateFormatted(date: any) {
    if (date == null) {
      return null;
    }
    let formattedDate = new Date(date);
    formattedDate.setDate(formattedDate.getDate() + 1); // Add one more day
    return formattedDate.toISOString().substring(0, 10);
  }


  handleRemoveFromWishList(event:any,product: any,index:any){
    event.stopPropagation();
    
    this.wishlistService.removeFromWishlist(product.wishlistId).subscribe({
      next: (res) => {

        this._ngZone.run(() => {
        this.toastService.showDeleteToast('Removed from wishlist', 'close');
        this.productList[index].isWishlisted=false;
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

  handleAddToWishList(event:any,product: any,index:any) {
    event.stopPropagation();
    this.wishlistService.addToWishlist(product.id).subscribe({
      next: (res) => {
        
        this.productList[index].isWishlisted=true;
        this.productList[index].wishlistId=res;
        this._ngZone.run(() => {
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



  handleAddToCart(event: MouseEvent, product: any) {
    event.stopPropagation();
    if (this.authService.isAuthenticated()) {
      this.startDate = this.dateFormatted(this.dateRange.start);
      this.endDate = this.dateFormatted(this.dateRange.end);
      this.cartService
        .addToCart(
          product.id,
          undefined,
          this.startDate ? this.startDate : undefined,
          this.endDate ? this.endDate : undefined
        )
        .subscribe({
          next: (res) => {
            this.currentCount = res.cartCount;
            localStorage.setItem('cartCount', this.currentCount.toString());
            this.cartStateService.updateCartCount(this.currentCount);
            // this.ngOnInit();
            this.router.navigate(['/cart']);
          },
        });
    } else {
      this.router.navigate(['/login']);
    }
    // addtocart Logic.
  }

  handleGoToCart(event: MouseEvent, product: any) {
    event.stopPropagation();
    this.router.navigate(['/cart']);
  }
  trimString(string: any) {
    if (string.length > 10) {
      return string.substring(0, 10) + '...';
    } else {
      return string;
    }
  }
}
