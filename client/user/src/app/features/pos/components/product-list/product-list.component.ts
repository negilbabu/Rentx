import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { DateAdapter } from '@angular/material/core';
import {
  ActivatedRoute,
  NavigationEnd,
  NavigationStart,
  Router,
} from '@angular/router';
import { UserCategoryService } from '../../services/user-category.service';
import { UserStoreService } from '../../services/user-store.service';
import { UserProductService } from '../../services/user-product.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { Subscription } from 'rxjs';
import { TrimPipe } from '@nglrx/pipes';
import { ProductsComponent } from '../products/products.component';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css'],
})
export class ProductListComponent implements OnInit {
  @ViewChild('filterBar') filterBar!: ElementRef<HTMLElement>;
  @ViewChild('sortMenu') sortMenu!: ElementRef<HTMLElement>;
  @ViewChild('categoryfilterMenu') categoryfilterMenu!: ElementRef<HTMLElement>;
  @ViewChild('categoryfilterMenuMobile')
  categoryfilterMenuMobile!: ElementRef<HTMLElement>;
  @ViewChild('storefilterMenu') storefilterMenu!: ElementRef<HTMLElement>;
  @ViewChild('storefilterMenuMobile')
  storefilterMenuMobile!: ElementRef<HTMLElement>;
  @ViewChild('selectedFilters') selectedFilters!: ElementRef<HTMLElement>;
  @ViewChild('dateRangeInputMobile') dateRangeInputMobile!: ElementRef;
  @ViewChild('dateRangeInputDesktop') dateRangeInputDesktop!: ElementRef;
  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });

  minDate: Date = new Date();
  maxDate!: Date;

  // minDate: Date = new Date();
  calendar: any;
  categories: any[] = [];
  stores: any[] = [];
  searchParam!: string;
  sortParm!: string;
  sortOrder!: string;
  pageNum = 1;
  categoryFilterParams: string[] = [];
  StoreFilterParams: string[] = [];
  selectedCategoryFilter!: string[];
  selectedStoreFilter!: string[];
  eventSubscription!: Subscription;
  lastpage!: any;
  startDate: any;
  tempLatitudeParam: any;
  tempLongitudeParam: any;
  @Input() latitudeParam!:any;
  @Input() longitudeParam!:any;


  endDate: any;
  startDateParam: any;
  endDateParam: any;
  constructor(
    private dateAdapter: DateAdapter<Date>,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private categoryService: UserCategoryService,
    private storeService: UserStoreService,
    private userProductService: UserProductService,
    private eventEmitter: EventEmitterService
  ) {
    const currentYear = new Date().getFullYear();
    this.maxDate = new Date(currentYear + 1, 11, 31);
    this.range.valueChanges.subscribe(() => {
      if (this.range.valid) {
        console.log(this.range.value);
        localStorage.setItem(
          'startDate',
          this.range.get('start')?.value?.toString() ?? ''
        );
        localStorage.setItem(
          'endDate',
          this.range.get('end')?.value?.toString() ?? ''
        );
      }
    });
  }
  // // flatpickrInstance
  // flatpickrInstance: flatpickr.Instance | null = null;

  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        window.scrollTo(0, 0); // Scroll to the top of the page
      }
    });

    // ______________________________________

    this.dateAdapter.setLocale('en'); // Set your desired locale here

    this.activatedRoute.queryParams.subscribe((params) => { 
      this.searchParam = params['search'] ? params['search'] : '';
      this.searchParam = decodeURIComponent(this.searchParam);
      this.sortParm = params['sortBy'];
      this.sortOrder = params['sortIn'];
      this.pageNum = params['page'] ? params['page'] : 1;

      this.categoryFilterParams = params['categoryFilterParams']
        ? params['categoryFilterParams'].split('_')
        : [];

      this.StoreFilterParams = params['storeFilterParams']
        ? params['storeFilterParams'].split('_')
        : [];

      this.selectedCategoryFilter = params['categoryFilterParams']
        ? params['categoryFilterParams']
            .split('_')
            .map((filter: any) => filter.split(':')[1])
        : [];
      this.selectedStoreFilter = params['storeFilterParams']
        ? params['storeFilterParams']
            .split('_')
            .map((filter: any) => filter.split(':')[1])
        : [];
      this.startDate;
      this.endDate;
      if(params['latitude']!=undefined){

      this.latitudeParam=params['latitude']
      this.longitudeParam=params['longitude']
      console.log(this.latitudeParam,"loginfinside product list");
    }
      
      // Call API with the received parameters
      this.loadData();
      console.log(this.selectedCategoryFilter, 'selected');
    });

    this.eventSubscription = this.eventEmitter
      .getonLoadEvent()
      .subscribe(() => {
        this.getlastPage();
      });
  }



  // getter the category name.
  getCategoryName(id: any) {
    const category = this.categories.find(
      (category) => category.id.toString() === id.toString()
    );
    if (category) {
      return category.name;
    }
    return 'Category not found';
  }
  // getter the store name.
  getStoreName(id: any) {
    const store = this.stores.find(
      (store) => store.id.toString() === id.toString()
    );
    if (store) {
      return store.name;
    }
    return 'Store not found';
  }

  getlastPage(): any {
    this.lastpage = localStorage.getItem('lastPage');
  }

  loadData() {
    console.log('load data');

    this.categoryService.listAllCategories().subscribe({
      next: (res) => {
        this.categories = res;
      },
      error: (err) => {
        // console.log(err);
      },
    });

    this.storeService.listAllStores().subscribe({
      next: (res) => {
        this.stores = res;
      },
      error: (err) => {
        // console.log(err);
      },
    });

    this.getlastPage();
  }

  // check that the filter is already selected.
  isCategorySelected(categoryId: number): boolean {
    return this.selectedCategoryFilter.includes(categoryId.toString());
  }
  // check that the filter is already selected.
  isStoreSelected(storeId: number): boolean {
    return this.selectedStoreFilter.includes(storeId.toString());
  }
  class = 'custom-input pointer-events-none';
  // dateFilterFn = (date: Date) => ![0, 6].includes(date.getDay());

  toggleElement(element: HTMLElement) {
    element.classList.toggle('hidden');
  }
  hasHiddenClass(element: HTMLElement): boolean {
    return element.classList.contains('hidden');
  }

  onCategoryFilterSubmit(checked: boolean, value: any) {
    console.log(checked, value);
    if (checked) {
      this.filterSelected(
        value,
        this.categories,
        this.categoryFilterParams,
        'categoryFilterParams'
      );
    } else {
      this.filterDeselected(
        value,
        this.categories,
        this.categoryFilterParams,
        'categoryFilterParams'
      );
    }

  }

  removeCategoryFilterFromCapsule(value: any) {
    console.log(value, 'va');
    this.filterDeselected(
      parseInt(value),
      this.categories,
      this.categoryFilterParams,
      'categoryFilterParams'
    );
  }
  removeStoreFilterFromCapsule(value: any) {
    this.filterDeselected(
      parseInt(value),
      this.stores,
      this.StoreFilterParams,
      'storeFilterParams'
    );
  }

  // fun() that handles store filter selection.
  onStoreFilterSubmit(checked: boolean, value: any) {
    console.log(checked, value);
    if (checked) {
      this.filterSelected(
        value,
        this.stores,
        this.StoreFilterParams,
        'storeFilterParams'
      );
    } else {
      this.filterDeselected(
        value,
        this.stores,
        this.StoreFilterParams,
        'storeFilterParams'
      );
    }
  }

  // fun() that handles selection of a filter.
  filterSelected(
    value: any,
    filters: any[],
    filterParams: string[],
    queryParamsKey: string
  ) {
    const filter = filters.find((item) => item.id === value);
    const filterValue = filter ? `${filter.name}:${value}` : value;

    filterParams.push(filterValue);
    this.pageNum = 1;
    
    this.updateQueryParams();
  }
  // fun() that handles deselection of a filter.
  filterDeselected(
    value: any,
    filters: any[],
    filterParams: string[],
    queryParamsKey: string
  ) {
    const filter = filters.find((item) => item.id === value);
    const filterValue = filter ? `${filter.name}:${value}` : value;

    filterParams = filterParams.filter((param) => param !== filterValue);
    console.log(filterParams, `${queryParamsKey} on deselect`);

    if (queryParamsKey === 'categoryFilterParams') {
      this.categoryFilterParams = filterParams;
    } else if (queryParamsKey === 'storeFilterParams') {
      this.StoreFilterParams = filterParams;
      console.log(this.StoreFilterParams, 'StoreFilterParams');
    }
    this.pageNum = 1;

    this.updateQueryParams();
  }
  // fun() that handles the sort functionality.
  onSortChange(sortValue: any, sortOrder: any) {
    this.sortMenu.nativeElement.classList.toggle('hidden');

    this.sortParm = sortValue;
    this.sortOrder = sortOrder;
    this.pageNum = 1;
    this.updateQueryParams();
  }
  // next  in the pagination.
  onNext() {
    this.pageNum++;
    console.log('onN', this.pageNum);
    this.updateQueryParams();
  }
  // previous in the pagination.
  onPrev() {
    this.pageNum--;
    this.updateQueryParams();
  }

  updateQueryParams() {
    let queryParams: {
      search?: string;
      sortBy?: string;
      sortIn?: string;
      page?: number;
      categoryFilterParams?: string;
      storeFilterParams?: string;
      latitudeParam?: any;
      longitudeParam?: any;
    } = {};

    if (this.sortParm) {
      queryParams.sortBy = this.sortParm;
    }
    if (this.sortOrder) {
      queryParams.sortIn = this.sortOrder;
    }
    if (this.pageNum) {
      queryParams.page = this.pageNum;
    }

    if (this.searchParam) {
      // queryParams.search = encodeURIComponent(this.searchParam);
      queryParams.search = this.searchParam;
    }

    if (this.categoryFilterParams.length > 0) {
      queryParams.categoryFilterParams = this.categoryFilterParams.join('_');
    }

    if (this.StoreFilterParams.length > 0) {
      queryParams.storeFilterParams = this.StoreFilterParams.join('_');
    }
    if(this.latitudeParam){
      console.log("inside latitude if condition"+this.latitudeParam);
      
      queryParams.latitudeParam=this.latitudeParam;
      queryParams.longitudeParam=this.longitudeParam;
    }

    console.log(queryParams, 'queryParams');

    // Navigate to the new URL
    this.router.navigate(['/products'], { queryParams });
  }

  getSortString() {
    if (this.sortParm === 'price') {
      if (this.sortOrder === 'DESC') {
        return 'Price: High to Low';
      } else if (this.sortOrder === 'ASC') {
        return 'Price: Low to High';
      }
    } else if (this.sortParm === 'updatedAt' && this.sortOrder === 'DESC') {
      return 'Newest';
    }
    // Default case if the combination doesn't match any condition
    return 'Newest';
  }

  editStartDate(startDate: any) {
    const date = new Date(startDate.target.value);
    date.setDate(date.getDate() + 1); // Add one more day
    const formattedDate = date.toISOString().substring(0, 10);
    this.startDate = formattedDate;
  }
  editEndDate(endDate: any) {
    const date = new Date(endDate.target.value);
    date.setDate(date.getDate() + 1); // Add one more day
    const formattedDate = date.toISOString().substring(0, 10);
    this.endDate = formattedDate;

    this.eventEmitter.getStartAndEndDate(this.startDate, this.endDate);
  }
}
