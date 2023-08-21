import { Component, OnInit, Renderer2 } from '@angular/core';
import { Subscription, Subject, debounceTime } from 'rxjs';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { StoreService } from '../../../services/store.service';
import { VendorOrderService } from '../../../services/vendor-order.service';

@Component({
  selector: 'app-vendor-order-list',
  templateUrl: './vendor-order-list.component.html',
  styleUrls: ['./vendor-order-list.component.css']
})
export class VendorOrderListComponent implements OnInit {
  eventSubscription: Subscription;
  private searchSubject = new Subject<string>();
  productListResponse: any;
  searchQuery: string = '';
  sortField = 'updatedAt';
  selectedCategory: any;
  Data = { lastPage: 0 };
  sortOrder: boolean = false;
  sortDirection: 'ASC' | 'DESC' = 'DESC';
  pageNo = 1;
  categoryList: any;
  isConfirm = false;
  responseLength: any;
  isLoading: boolean = false;
  itemCount: any;
  currentPage = 1;

  Responsedata: any;
  productList: any;
  orderList: any;
  ngOnInit(): void {
    this.loadData();
    this.searchSubject.pipe(debounceTime(500)).subscribe((searchTerm) => {
      // Perform search here
      this.Search(searchTerm);
    });
  }

  constructor(
    private renderer: Renderer2,
    private eventEmitter: EventEmitterService,
    public toast: ToastService,
    private vendorOrder: VendorOrderService,
    public alertService: AlertService
  ) {
    this.eventSubscription = this.eventEmitter
      .getonSaveEvent()
      .subscribe(() => {
        this.loadData();
      });
  }
  loadData() {
    this.isLoading=true;
    this.vendorOrder.getOrderHistoryList(
        this.searchQuery,
        this.sortField,
        this.sortDirection,
        this.currentPage
      )
      .subscribe((res) => {
        
         this.orderList=res.result;
        // this.orderList=res.result;
        this.isLoading=false;
        console.log('hfhhfhfhfh',res.result)
        this.Data = res;
        this.currentPage = res.currentPage;
        this.productList = res.result;
        console.log(this.productList);
        this.itemCount = res.result.length;
        this.responseLength = res.numItems;
        console.log("res len=",this.responseLength);
        
        
      });
      
  }

  // sort direction toggle.
  toggleSortDirection(sortDirection: 'ASC' | 'DESC'): 'ASC' | 'DESC' {
    return this.sortDirection === 'ASC' ? 'DESC' : 'ASC';
  }
  // onCategoryChange() {
  //   this.productService
  //     .listAllProducts(
  //       this.searchQuery,
  //       this.sortField,
  //       this.sortDirection,
  //       1,
  //       this.selectedCategory
  //     )
  //     .subscribe((res) => {
  //       this.Data = res;
  //       this.productList = res.result;
  //       console.log(this.productList);
  //       this.itemCount = res.result.length;
  //       this.currentPage = 1;
  //       this.responseLength = res.numItems;
  //     });
  //   // Perform any actions based on the selected category ID
  // }
  // getAllCategory() {
  //   this.productService.getCategory().subscribe({
  //     next: (data: any) => {
  //       this.categoryList = data;
  //       console.log('Category = ', this.categoryList);
  //     },
  //     error: () => {},
  //   });
  // }
  // requestHandle(product: any) {
  //   this.isConfirm = true;
  //   this.alertService
  //     .openAlert('Do you want to delete the product?')
  //     .subscribe((res) => {
  //       if (res) {
  //         this.deleteProduct(product.id);
  //       } else {
  //         this.isConfirm = false;
  //         return;
  //       }
  //     });
  // }
  detailViewNavigation(productId: any) {}

  // deleteProduct(id: any) {
  //   this..deleteProduct(id).subscribe({
  //     next: (res) => {
  //       this.eventEmitter.onSaveEvent();
  //       this.isConfirm = false;
  //       if (this.itemCount == 1) {
  //         this.currentPage = this.currentPage - 1;
  //         this.loadData();
  //       }
  //       this.toast.showDeleteToast('Product Deleted', 'close');
  //     },
  //   });
  // }
  sort(sortField: string) {
    this.sortField = sortField;
    this.sortDirection = this.toggleSortDirection(this.sortDirection);
    this.vendorOrder.getOrderHistoryList(
        this.searchQuery,
        this.sortField,
        this.sortDirection,
        1,
        // this.selectedCategory
      )
      .subscribe({
        next: (res: any) => {
          
          this.orderList=res.result;
          this.productList = res.result;
          this.responseLength = res.numItems;
          this.currentPage = res.currentPage;
          this.Data = res;
          this.currentPage = 1;
        },
        error: () => {},
      });
  }

  // onSearchEventListner
  onSearch(event: Event) {
    const searchTerm = (event.target as HTMLInputElement).value;
    this.searchSubject.next(searchTerm);
    this.Search(searchTerm);
  }

  // Search function.
  Search(searchTerm: string) {
    // if(searchTerm.trim()!=''){
    if (searchTerm.trim().length === 0 || searchTerm[0] === ' ') {
      // No characters in the search term or first character is whitespace
      if (searchTerm.length == 0) {
        searchTerm = encodeURIComponent(searchTerm);
        this.searchQuery = searchTerm;

        this.vendorOrder
          .getOrderHistoryList(
            this.searchQuery,
            this.sortField,
            this.sortDirection,
            1,
            this.selectedCategory
          )
          .subscribe({
            next: (res: any) => {
              this.orderList=res.result;
              this.currentPage = res.currentPage;
              this.Data = res;
              this.productList = res.result;
              this.pageNo = res.currentPage;
              this.responseLength = res.numItems;
            },
            error: () => {},
          });
      }

      return;
    }

    searchTerm = encodeURIComponent(searchTerm);
    this.searchQuery = searchTerm;

    this.vendorOrder
      .getOrderHistoryList(
        this.searchQuery,
        this.sortField,
        this.sortDirection,
        1,
        this.selectedCategory
      )
      .subscribe({
        next: (res: any) => {
          this.orderList=res.result;
          this.Data = res;
          this.productList = res.result;
          this.currentPage = res.currentPage;
          this.currentPage = res.firstPage;
          this.responseLength = res.numItems;
        },
        error: () => {},
      });
  }

  clearSearch() {
    const inputElement = document.querySelector(
      'input[name="searchField"]'
    ) as HTMLInputElement;
    if (inputElement) {
      inputElement.value = '';
      this.searchSubject.next('');
    }
  }
  
  Pagenate(pageNo: any) {
    this.isLoading = true; // Set isLoadingData to true before making the API call

    setTimeout(() => {
      this.vendorOrder
        .getOrderHistoryList(
          this.searchQuery,
          this.sortField,
          this.sortDirection,
          pageNo,

        )
        .subscribe((res) => {
          console.log(res.result)
          this.orderList=res.result;
          this.productList = res.result;
          this.currentPage = res.currentPage;
          this.itemCount = res.result.length;
          this.isLoading = false;
          this.responseLength = res.numItems;
          this.isLoading = false; // Set isLoadingData to false after receiving the API response
          console.log('num item in next =',res.numItems);
          console.log('num item in next =', this.itemCount);

       

        });
    }, 20);
  }
}
