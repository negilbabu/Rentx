import { Component, HostListener, NgZone, OnInit } from '@angular/core';
import { OrderServiceService } from '../../Services/order-service.service';
import { AddresListService } from 'src/app/features/profile-management/services/addres-service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CartStateService } from 'src/app/shared/states/cart-state.service';
import { cartToCheckoutService } from 'src/app/shared/states/cartToCheckout.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { Router } from '@angular/router';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { AlertService } from 'src/app/shared/services/alert.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  paymentMethodData: any;


  addressForm: FormGroup = new FormGroup({
    addressId: new FormControl('', [
      Validators.required,

    ]),
  });
  paymentMethodForm: FormGroup = new FormGroup({
    paymentId: new FormControl('', [
      Validators.required,

    ]),
  });

  panelOpenState = false;
  isRemarkActive: boolean = true;
  accordionItem1: any;
  accordionItem2: any;
  accordionItem3: any;
  accordionItem4: any;
  cartData: any;
  currentCount: any;
  totalPrice: any;
  addressDivActivator=true;
  panel1Expanded = false; // First accordion is open by default
  panel2Expanded = false;
  panel3Expanded = false;
  panel4Expanded = false;
  loginTick= false;
  addressTick= false;
  orderSummaryTick= false;
  paymentTick=false;
  isExpansionDisabled: boolean = true;
allwaysFalseFlag=false;
expantionChecker: true | false = true;
  pointerLogin: string ='none';
  pointerAddress: string='none';
  pointerSummary: string='none';
  pointerPayment: string='none';
  addressList: any;
  cartId: any;
  formSubmitted: any;
  AddressDetailsForm!: FormGroup;
  editAddressDetailsForm!: FormGroup;
  isConfirm:any;
    addbuttonDisabler:any;
    editButtonDisabler:any;
    phoneNumberRegexp = new RegExp(/^[0-9]{10,15}$/);
    pincodeRegex = new RegExp(/^[0-9]{6,8}$/);
    nameRegex = new RegExp(/^[a-zA-Z0-9_\s]{3,20}$/);
    roadRegex = new RegExp(
      /^(?!\s)[a-zA-Z0-9_\s!@#$%^&*()-+=~`{}[\]|:;"'<>,.?/]{3,20}(?<!\s)$/
    );
  actualData: any;
  vatIncludedPrice: any;
  currentAddress: any;
  addressId:any;
  editDivActivater=false;
  orderAddress:any;
  orderPayment:any;
  accordion: any;



  constructor(private orderService:OrderServiceService,
    private toastService: ToastService,
    private _ngZone: NgZone,
    private route: Router,
    private eventEmitter:EventEmitterService, 
    private addressService:AddresListService,
    private alertService:AlertService,
    private cartToCheckout:cartToCheckoutService) { }

  ngOnInit(): void {
    this.formSubmitted=false;
    this.addbuttonDisabler=true;
    this.editButtonDisabler=true;
    this.openAddressForm();
    this.openEditAddressForm();
    this.cartToCheckout.checkoutCart$.subscribe((count) => {
      this.cartId = count;
     
      if(count==0){
        this.route.navigateByUrl('/cart')
      }
      console.log(this.cartId)
      if(count!=0){
        this.getOrderData();
      }
    });
    this.getOrderData();
    this.getAddress();
    if (localStorage.getItem('token')){
      this.loginTick=true;
    }
this.openDeliveryAdress();
  }


  resetAccordion(){

this.openDeliveryAdress();
  }


  openAddressForm(){
    this.AddressDetailsForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]),
      phone: new FormControl('', [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(15),
        Validators.pattern(this.phoneNumberRegexp),
      ]),
      city: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]),
      state: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]),
      pinCode: new FormControl('', [
        Validators.required,
        Validators.maxLength(8),
        Validators.pattern(this.pincodeRegex),
      ]),
      houseName: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]),
   
      type: new FormControl('', [
        Validators.required,
        
      ]),
    });
  }

  openEditAddressForm(){
    this.editAddressDetailsForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]),
      phone: new FormControl('', [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(15),
        Validators.pattern(this.phoneNumberRegexp),
      ]),
      city: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]),
      state: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]),
      pinCode: new FormControl('', [
        Validators.required,
        Validators.maxLength(8),
        Validators.pattern(this.pincodeRegex),
      ]),
      houseName: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]),
   
      type: new FormControl('', [
        Validators.required,
        
      ]),
    });
  }


  getOrderData(){
    // const cartObject = { cartId: this.cartId };
    this.orderService.getOrderData(this.cartId).subscribe((res: any) => {
      console.log("lee", res);
      this.actualData=res;
      this.cartData = res.orderSummeryListProductPager.result;
      this.totalPrice = res.totalPrice;
      this.vatIncludedPrice = res.priceIncludeVat;
      this.currentCount=res.orderSummeryListProductPager.numItems;
      console.log(this.cartData);

    },);
  }
  addNewAddress() {
    this.addressForm.reset();
    this.orderAddress=null;
this.addressDivActivator=false;
this.editDivActivater=false;
    }

openLogin(){
  this.panel1Expanded = false; // First accordion is open by default
  this.panel2Expanded = false;
  this.panel3Expanded = false;
  this.panel4Expanded = false;
  this.pointerLogin ='auto';
  this.pointerAddress='none';
  this.pointerSummary='none';
  this.pointerPayment='none';
}

openDeliveryAdress(){  
this.panel1Expanded = false; // First accordion is open by default
this.panel2Expanded = true;
this.panel3Expanded = false;
this.panel4Expanded = false;
this.pointerLogin ='none';
this.pointerAddress='auto';
this.pointerSummary='none';
this.orderSummaryTick=false;
this.addressTick=false;
this.pointerPayment='none';
}

openOrderSummary(){
  this.panel1Expanded = false; // First accordion is open by default
  this.panel2Expanded = false;
  this.panel3Expanded = true;
  this.panel4Expanded = false;
  this.addressTick=true;
  this.orderSummaryTick=false;
  this.pointerLogin ='none';
  this.pointerAddress='none';
  this.pointerSummary='auto';
  this.pointerPayment='none';
  this.editDivActivater=false;
  this.addressDivActivator=true;
  this.orderPayment=null;

}

openPaymentMethod(){
  this.panel1Expanded = false; // First accordion is open by default
  this.panel2Expanded = false;
  this.panel3Expanded = false;
  this.panel4Expanded = true;
  this.orderSummaryTick=true;
  this.pointerLogin ='none';
  this.pointerAddress='none';
  this.pointerSummary='none';
  this.pointerPayment='auto';
  this.getPaymentMethods();
}


placeOrder(){
  this.paymentTick=false;
  this.panel1Expanded = false; 
  this.panel2Expanded = false;
  this.panel3Expanded = false;
  this.panel4Expanded = false;
  this.orderService.placeOrder(this.cartId,this.orderAddress,this.orderPayment).subscribe({
    next: (data: any) => {
      this.toastService.showSucessToast('Order Placed' , 'close');
      this.route.navigate(['/cart']);
      this.currentAddress=data;
    },
    error: (error:any) => {
      this.toastService.showErrorToast(error.error.errorMessage, 'close');
      this._ngZone.run(() => {
        this.eventEmitter.onSaveEvent();
        this.route.navigate(['/cart']);
      });
    },
  });

}

stayOnSummary() {
  this.panel1Expanded = false; // First accordion is open by default
  this.panel2Expanded = false;
  this.panel3Expanded = true;
  this.panel4Expanded = false;
  this.addressTick=true;
  this.pointerLogin ='none';
  this.pointerAddress='none';
  this.pointerSummary='auto';
  this.pointerPayment='none';

  }

disableExpansion(event:any) {
event.stopPropagation;
}

getAddress(){
  this.addressService.getAddressList().subscribe({
    next: (data: any) => {
      this.addressList = data;
      console.log('Addrses = ', this.addressList);
    },
    error: () => {},
  });
}
pickAddress(event:any, address:any)
{
this.orderAddress=address.id;
console.log(this.addressForm.value.addressId)
}

onAddressDetailsSubmit() {

      this.formSubmitted = true;
      
      if(this.AddressDetailsForm.valid){
        this.addbuttonDisabler=true;

  
  
        this.addressService.addAddress(this.AddressDetailsForm.value).subscribe({
          next: (data: any) => {
            this.toastService.showSucessToast('Address added sucessfully', 'close');
            this.ngOnInit();
            this._ngZone.run(() => {
              this.orderAddress=null;
              this.addressDivActivator=true;
            });
          },
          error: (error:any) => {
            this.toastService.showErrorToast(error.error.errorMessage, 'close');
            this._ngZone.run(() => {
              this.eventEmitter.onSaveEvent();
              this.route.navigate(['/profile/listAddress']);
            });
          },
        });
      }
      else{
        this.addbuttonDisabler=false;
      }
    }

    addressFormOff(){

      this.addressDivActivator=true;
      this.formSubmitted=false;
      this.orderAddress=null;
this.ngOnInit();
      // this.orderAddress=null;
    }
    addressEditFormOff(){
      this.editDivActivater=false;
      this.formSubmitted=false;
      this.orderAddress=null;
      this.ngOnInit();
    }

    deleteAddress(addressId:any){
      this.addressService.deleteAddress(addressId).subscribe({
        next: (res) => {
          this._ngZone.run(() => {
          this.eventEmitter.onDeleteEvent();
          this.isConfirm = false;
          this.orderAddress=null;
          this.toastService.showDeleteToast('Address Deleted', 'close'); 
          this.getAddress();
        });
        },
      });
  
    }


    pickPayment(event:any, payment:any) {
this.orderPayment=payment.id;

console.log(payment.id)
      }

  
    requestHandle(address: any) {
      this.isConfirm = true;
      this.alertService
        .openAlert('Do you want to delete the address?')
        .subscribe((res) => {
          if (res) {
            this.deleteAddress(address.id);
          } else {
            this.isConfirm = false;
            return;
          }
        });
    }
  

    getAddressById(data:any){
this.editDivActivater=true;
this.addressDivActivator=true;
this.addressId=data;
      this.addressService.getAddressById(data).subscribe({
        next: (data: any) => {
          this.currentAddress=data;
        },
        error: (error:any) => {
          this.toastService.showErrorToast(error.error.errorMessage, 'close');
          this._ngZone.run(() => {
            this.eventEmitter.onSaveEvent();
            this.route.navigate(['/profile/listAddress']);
          });
        },
      });
  
    }
  
    updateAddress(){
      this.editButtonDisabler=false;
      if(this.editAddressDetailsForm.valid){
        this.editButtonDisabler=false;
console.log(this.editAddressDetailsForm.value)
      this.addressService.updateAddress(this.addressId,this.editAddressDetailsForm.value).subscribe({
        next: (data: any) => {
          this.toastService.showSucessToast('Address Updated', 'close');
          this._ngZone.run(() => {
            this.orderAddress=null;
          this.currentAddress=data;
          this.editDivActivater=false;
          this.ngOnInit();
       });
        },
        error: (error:any) => {
          this.toastService.showErrorToast(error.error.errorMessage, 'close');
          this._ngZone.run(() => {
      
          });
        },
      });
    }
    else{this.editButtonDisabler=false;}
    }

  getPaymentMethods(){
 this.orderService.getPaymentMethod().subscribe((res: any) => {
  console.log("leho", res);
  this.paymentMethodData=res;
  console.log(this.cartData);

},);
}

@HostListener('document:keydown.Space', ['$event'])
handleKeyDown(event: KeyboardEvent) {
  event.stopImmediatePropagation();
  // Handle the space key press event
  console.log('Space key pressed!');
}




}


