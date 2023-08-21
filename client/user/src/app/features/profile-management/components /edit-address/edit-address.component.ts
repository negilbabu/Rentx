import { Component, NgZone, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastService } from 'src/app/shared/services/toast.service';
import { AddresListService } from '../../services/addres-service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import * as MapboxGeocoder from '@mapbox/mapbox-gl-geocoder';
import { environment } from 'src/environments/environment';
@Component({
  selector: 'app-edit-address',
  templateUrl: './edit-address.component.html',
  styleUrls: ['./edit-address.component.css']
})
export class EditAddressComponent implements OnInit {

 
formSubmitted: any;
AddressDetailsForm!: FormGroup;

  buttonDisabler=false;
  phoneNumberRegexp = new RegExp(/^[0-9]{10,15}$/);
  pincodeRegex = new RegExp(/^[0-9]{6,8}$/);
  nameRegex = new RegExp(/^[a-zA-Z0-9_\s]{3,20}$/);
  roadRegex = new RegExp(
    /^(?!\s)[a-zA-Z0-9_\s!@#$%^&*()-+=~`{}[\]|:;"'<>,.?/]{3,20}(?<!\s)$/
  );
  addressId: any;
  currentAddress: any;
  isCityValid = false;
  citySearchFlag: true | false = false;

  formattedData = {
    city: '',
    state: '',
  };

  
  constructor( private eventEmitter: EventEmitterService,

    private toastService: ToastService,
    private _ngZone: NgZone,
    private route: Router,
    private router: ActivatedRoute,
    private ngxService: NgxUiLoaderService,
     private addressService:AddresListService) { }

  ngOnInit(): void {
 
    this.getAddress(this.router.snapshot.params['id']);
    this.addressId=this.router.snapshot.params['id'];
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

    (MapboxGeocoder as any).accessToken = environment.mapboxAccessToken;
    let city = '';

  }
  setLatLong() {
    // // const lat = Math.random() * 180 - 90;
    // // const long = Math.random() * 360 - 180;
    // if (this.citySearchFlag != true) {
    //   return {
    //     city: this.storeDetails.city,
    //     lattitude: this.storeDetails.lattitude,
    //     longitude: this.storeDetails.longitude,
    //   };
    // } else {
      return {
        city: this.formattedData.city,
        // lattitude: this.formattedData.latitude,
        // longitude: this.formattedData.longitude
      };
    // }
  }
  changeLocation() {
    console.log('before search flag', this.citySearchFlag);

    this.citySearchFlag = this.toggleSearchCityView(this.citySearchFlag);
    console.log('after search flag', this.citySearchFlag);
    this.handleMapBox();
  }

  toggleSearchCityView(citySearchFlag: true | false): true | false {
    return this.citySearchFlag === true ? false : true;
  }

  getAddress(data:any){

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
    if(this.AddressDetailsForm.valid){
    this.addressService.updateAddress(this.addressId,this.AddressDetailsForm.value).subscribe({
      next: (data: any) => {
        this.toastService.showSucessToast('Address Updated', 'close');
        this.currentAddress=data;
        this.route.navigate(['/profile/listAddress']);
        this.citySearchFlag = false;
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
  }

  handleMapBox() {
    const searchBoxElement = document.getElementById('search-box');
    // Check if searchBoxElement is null before accessing its properties
    const existingGeocoder = searchBoxElement?.querySelector(
      '.mapboxgl-ctrl-geocoder'
    );
    if (existingGeocoder) {
      existingGeocoder.remove(); // Remove the existing Mapbox Geocoder instance
    }
    const geocoder = new (MapboxGeocoder as any)({
      accessToken: (MapboxGeocoder as any).accessToken,
      types: 'place',
      placeholder: 'Choose city',
      countries: 'IN',
      container: 'search-box',

      bbox: [68.1, 6.5, 97.4, 35.5], // Bounding box for India
    });

    geocoder.addTo('#search-box');

    geocoder.on('result', (e: any) => {
      this.isCityValid = true;
      // Handle the selected location result
      console.log('---------------', e.result.text); // Display the details of the selected location
      // Formating the response data
      this.formattedData = {
        city: e.result.text || '',

        state:
          e.result.context.find((c: any) => c.id.includes('region'))?.text ||
          ''
      };
      console.log(this.formattedData);
    });

    geocoder.on('clear', () => {
      this.isCityValid = false;
      this.formattedData.state = '';
      this.formattedData.city = '';
    });
  }


}

  


