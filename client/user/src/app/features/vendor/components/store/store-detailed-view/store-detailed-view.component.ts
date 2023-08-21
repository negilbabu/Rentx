import { Component, NgZone, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators,
  FormBuilder,
  FormArray,
} from '@angular/forms';
import { ToastService } from 'src/app/shared/services/toast.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { Subscription } from 'rxjs';
import { StoreService } from '../../../services/store.service';
import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { AlertService } from 'src/app/shared/services/alert.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import * as mapboxgl from 'mapbox-gl';
import * as MapboxGeocoder from '@mapbox/mapbox-gl-geocoder';
import { environment } from 'src/environments/environment';
@Component({
  selector: 'app-store-detailed-view',
  templateUrl: './store-detailed-view.component.html',
  styleUrls: ['./store-detailed-view.component.css'],
})
export class StoreDetailedViewComponent implements OnInit {
  storeDetails: any;
  editStoreForm!: FormGroup;
  formSubmitted = false;
  storeId: any;
  currentCity: any;
  selectedCity: any;
  errorData: any;
  iseditable: true | false = false;
  buttonDisabler = false;
  isCityValid = false;
  citySearchFlag: true | false = false;
  tempName: any = 1;

  phoneNumberRegexp = new RegExp(/^[0-9]{10,15}$/);
  pincodeRegex = new RegExp(/^[0-9]{6,8}$/);
  nameRegex = new RegExp(/^[a-zA-Z0-9_\s]{3,20}$/);
  roadRegex = new RegExp(
    /^(?!\s)[a-zA-Z0-9_\s!@#$%^&*()-+=~`{}[\]|:;"'<>,.?/]{3,20}(?<!\s)$/
  );
  formattedData = {
    city: '',
    district: '',
    state: '',
    country: '',
    longitude: '',
    latitude: '',
  };
  latitude: any;
  longitude: any;
  constructor(
    private eventEmitter: EventEmitterService,
    private toastService: ToastService,
    private storeService: StoreService,
    private _ngZone: NgZone,
    private route: Router,
    private formBuilder: FormBuilder,
    private router: ActivatedRoute,
    private errorCodeHandle: ApiErrorHandlerService,
    private ngxService: NgxUiLoaderService,

  ) {}

  ngOnInit(): void {
    this.storeId = this.router.snapshot.params['id'];
    console.log('store id =', this.storeId);
    console.log('city flag ===', this.citySearchFlag);
    console.log('is editable flag ===', this.iseditable);

    this.loadData();

    this.editStoreForm = this.formBuilder.group({
      name: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]),
      mobile: new FormControl('', [
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(15),
        Validators.pattern(this.phoneNumberRegexp),
      ]),
      buildingName: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]),
      pincode: new FormControl('', [
        Validators.required,
        Validators.maxLength(8),
        Validators.pattern(this.pincodeRegex),
      ]),
      city: new FormControl('', [
        Validators.required,
        // Validators.maxLength(20),
        // Validators.pattern(this.roadRegex),
      ]),
      district: new FormControl('', []),
      state: new FormControl('', []),
      country: new FormControl('', []),
    });

    (MapboxGeocoder as any).accessToken = environment.mapboxAccessToken;
    let city = '';
  }
  // return an object with a random latitude and longitude
  setLatLong() {
    // const lat = Math.random() * 180 - 90;
    // const long = Math.random() * 360 - 180;
    if (this.citySearchFlag != true) {
      return {
        city: this.storeDetails.city,
        lattitude: this.storeDetails.lattitude,
        longitude: this.storeDetails.longitude,
      };
    } else {
      return {
        city: this.formattedData.city,
        lattitude: this.formattedData.latitude,
        longitude: this.formattedData.longitude,
      };
    }
  }

  toggleEditOrView(iseditable: true | false): true | false {
    // this.handleMapBox();
    console.log('2', this.iseditable);

    return this.iseditable === true ? false : true;
  }
  toggleSearchCityView(citySearchFlag: true | false): true | false {
    return this.citySearchFlag === true ? false : true;
  }

  getMyStyles() {
    let myStyles = {
      color: this.iseditable ? 'blue' : 'black',
    };
    return myStyles;
  }

  // Toggle function
  editHandle() {
    this.iseditable = this.toggleEditOrView(this.iseditable);
    console.log('search flag', this.citySearchFlag);

    if (this.iseditable == false) {
      this.loadData();
  
      this.editStoreForm.patchValue(this.storeDetails);
      this.citySearchFlag=false;
      // this.loadData();
    }else{
      this.loadData();

    }

  }
  changeLocation() {
    console.log(' before iseditable', this.iseditable);
    this.citySearchFlag = this.toggleSearchCityView(this.citySearchFlag);
    // this.iseditable = this.toggleEditOrView(this.iseditable);

    console.log('iseditable', this.iseditable);
    console.log('search flag', this.citySearchFlag);
    this.handleMapBox();
  }

  //update store
  onStoreUpdateSubmit() {
    this.formSubmitted = true;
    console.log('formSubmitted', this.formSubmitted);
    console.log('pin =', this.editStoreForm.value.pincode);


    if (this.citySearchFlag == true) {
      this.editStoreForm.get('city')?.setValue(this.formattedData.city);
    } else {
      this.setLatLong();
    }

    console.log('form valid', this.editStoreForm.controls);

    if (this.editStoreForm.valid) {
      console.log('inside if', this.buttonDisabler);

      this.buttonDisabler = true;

      // this.ngxService.start();
      const storeDetailsData = {
        ...this.editStoreForm.value,
        ...this.setLatLong(),
      };
      console.log(storeDetailsData);

      this.storeService.editStore(this.storeId, storeDetailsData).subscribe({
        next: (data: any) => {
          this.iseditable = false;
          this.citySearchFlag = false;
          this.loadData();
          this.toastService.showSucessToast('Store Updated', 'close');
          this._ngZone.run(() => {
            this.eventEmitter.onSaveEvent();
            // this.route.navigate(['/vendor/stores']);
          });
        },
        error: (error:any) => {
          this.ngxService.stop();
          this.buttonDisabler = false;
    
              this._ngZone.run(() => {
                // this.route.navigate(['/vendor/addStore']);
                this.errorData
              });
              this.errorData = this.errorCodeHandle.getErrorMessage(
                error.error.errorCode
              );
              if (this.errorData === '007') {
                this.route.navigateByUrl('/error');
              } else {
                this.toastService.showErrorToast(this.errorData, 'close');
              }
        },
      });
    }
  }

  loadData() {
    this.storeService.getStoreDetail(this.storeId).subscribe({
      next: (data: any) => {
        console.log('data--', data);
        this.storeDetails = data;
        this.currentCity = data.city;

        this.handleMapBox();
      },
      error: () => {},
    });
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

        district:
          e.result.context.find((c: any) => c.id.includes('district'))?.text ||
          '',
        state:
          e.result.context.find((c: any) => c.id.includes('region'))?.text ||
          '',
        country:
          e.result.context.find((c: any) => c.id.includes('country'))?.text ||
          '',
        latitude: e.result.center[1],
        longitude: e.result.center[0],
      };
      this.storeDetails.city = this.formattedData.city;
      console.log('city1=', this.storeDetails.city);

      console.log(this.formattedData);
    });

    geocoder.on('clear', () => {
      this.isCityValid = false;
      this.formattedData.state = '';
      this.formattedData.district = '';
      this.formattedData.country = '';
      this.storeDetails.city = '';
    });
  }

  getLocation(): void {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          this.latitude = position.coords.latitude;
          this.longitude = position.coords.longitude;
          this.tempName = 0;
        },
        (error) => console.error(error)
      );
    } else {
      console.error('Geolocation is not supported by this browser.');
    }
    if (this.tempName == 0) console.log('latitude', this.latitude);
  }
}
