import {
  Component,
  ElementRef,
  HostListener,
  Input,
  OnInit,
  ViewChild,
} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserProductService } from 'src/app/features/pos/services/user-product.service';
import { AuthService } from 'src/app/services/auth.service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { CartStateService } from 'src/app/shared/states/cart-state.service';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css'],
})
export class NavComponent implements OnInit {
  @ViewChild('sidebar') sidebar!: ElementRef;
  @ViewChild('megaMenu') megaMenu!: ElementRef;
  @ViewChild('profileMenu') profileMenu!: ElementRef;
  @ViewChild('searchInput', { static: false }) searchInput!: ElementRef;
  @ViewChild('searchInputMobile', { static: false })
  searchInputMobile!: ElementRef;

  isAuthenticated: boolean = false;
  Message = 'Are you sure you want to log out ?';
  searchQuery: string = '';
  latitude: any;
  longitude: any;
  tempName:any=1;


  
  isMenuOpen: boolean = false;
  @Input() searchParam!: string;
  @Input() latitudeParam!:any;
  @Input() longitudeParam!:any;
  cartCount: number = 0;
  constructor(
    private authService: AuthService,
    public alertService: AlertService,
    private userProductService: UserProductService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private cartService: CartStateService
  ) {}

  ngOnInit(): void {
    // localStorage.setItem('cartCount','0');
    this.isAuthenticated = this.authService.isAuthenticated();
    console.log(this.searchParam, 'searc param');
    if (this.searchParam == undefined) {
      this.searchParam = '';
    }
    // this.searchParam = decodeURIComponent(this.searchParam);
    this.cartService.cartCount$.subscribe((count) => {
      this.cartCount = count;
    });
  }

  logOut() {
    this.alertService.openAlert(this.Message).subscribe((res) => {
      if (res) {
      this.removeCartCountOnLogOut()
        this.toggleprofileMenu();
        this.ngOnInit();
      } else {
        return;
      }
    });
  }
removeCartCountOnLogOut(){
  localStorage.removeItem('cartCount');
  this.cartService.updateCartCount(undefined);
  this.authService.logOut();
}

  toggleSidebar() {
    const sidebarEl = this.sidebar.nativeElement;
    sidebarEl.classList.toggle('hidden');
  }
  toggleMegaMenu() {
    const megaMenuEl = this.megaMenu.nativeElement;
    megaMenuEl.classList.toggle('hidden');
  }
  toggleprofileMenu() {
    const profileMenuEl = this.profileMenu.nativeElement;
    profileMenuEl.classList.toggle('hidden');
  }

  // isMenuOpened: boolean = false;

  toggleMenu(): void {
    const profileMenuEl = this.profileMenu?.nativeElement;
    profileMenuEl?.classList.add('hidden');
  }

  onSearch(searchQuery: any) {
    let queryParams: {
      search?: string;
      latitude?:any;
      longitude?:any;
    } = {};
    queryParams.search = encodeURIComponent(searchQuery);
    if(this.latitude!=''){
    
      queryParams.latitude=this.latitude;
      queryParams.longitude=this.longitude;
    }
    this.router.navigate(['/products'], { queryParams });
  }
  clearSearch() {
    this.searchParam = '';
    this.onSearch('');
  }
  clearInputField() {
    this.searchInput.nativeElement.value = '';
    this.searchInputMobile.nativeElement.value = '';
  }
  // }

  updateQueryParams() {
    let queryParams: {
      search?: string;
      latitude?:any;
      longitude?:any;


    } = {};

    if (this.searchParam) {
      queryParams.search = encodeURIComponent(this.searchParam);
    }
    if(this.latitude){
      console.log("inside nav"+this.latitude);
      
    
    queryParams.latitude=this.latitude;
    queryParams.longitude=this.longitude;
    }
    

    console.log(queryParams, 'queryParams');

    // Navigate to the new URL
    this.router.navigate(['/products'], { queryParams });
  }

  getLocation(): void {
    console.log("inside getLocation");
    
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        position => {
          this.latitude = position.coords.latitude;
          this.longitude = position.coords.longitude;
          console.log("latitude", this.latitude);
          console.log("longitude", this.longitude);
          localStorage.setItem("locationAccess","true");
          this.updateQueryParams();
        },
        error => {
          localStorage.setItem("locationAccess","false");
          console.error(error)
        }
      );
    } else {
      localStorage.setItem("locationAccess","false");
      console.error('Geolocation is not supported by this browser.');
    }
  }
    
}
