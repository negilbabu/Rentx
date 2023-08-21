import { Component, NgZone, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastService } from 'src/app/shared/services/toast.service';
import { AddresListService } from '../../services/addres-service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import * as MapboxGeocoder from '@mapbox/mapbox-gl-geocoder';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-add-address',
  templateUrl: './add-address.component.html',
  styleUrls: ['./add-address.component.css'],
})
export class AddAddressComponent implements OnInit {
  formSubmitted = false;
  AddressDetailsForm!: FormGroup;

  buttonDisabler = false;

  isCityValid = false;


  formattedData = {
    city: '',
    state: '',
  };

  phoneNumberRegexp = new RegExp(/^[0-9]{10,15}$/);
  pincodeRegex = new RegExp(/^[0-9]{6,8}$/);
  nameRegex = new RegExp(/^[a-zA-Z0-9_\s]{3,20}$/);
  roadRegex = new RegExp(
    /^(?!\s)[a-zA-Z0-9_\s!@#$%^&*()-+=~`{}[\]|:;"'<>,.?/]{3,20}(?<!\s)$/
  );

  constructor(
    private eventEmitter: EventEmitterService,

    private toastService: ToastService,
    private _ngZone: NgZone,
    private route: Router,
    private errorCodeHandle: ApiErrorHandlerService,
    private ngxService: NgxUiLoaderService,
    private addressService: AddresListService
  ) {}

  ngOnInit(): void {
    this.formSubmitted = false;
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

      type: new FormControl('', [Validators.required]),
    });

    (MapboxGeocoder as any).accessToken = environment.mapboxAccessToken;
    let city = '';
    this.handleMapBox();


  }
  // return an object with a random latitude and longitude

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

  onAddressDetailsSubmit() {



    this.formSubmitted = true;

    this.AddressDetailsForm.get('city')?.setValue(this.formattedData.city);

    if (this.AddressDetailsForm.valid) {
      this.buttonDisabler = true;

      // this.ngxService.start();
      const addressDetailsData = {
        ...this.AddressDetailsForm.value,
        ...this.setLatLong(),
      };
      console.log(addressDetailsData);


    console.log(this.AddressDetailsForm.value);

    if (this.AddressDetailsForm.valid) {
      this.buttonDisabler = true;

      this.addressService.addAddress(addressDetailsData).subscribe({
        next: (data: any) => {
          this.toastService.showSucessToast(
            'Address added sucessfully',
            'close'
          );
          this._ngZone.run(() => {
            this.eventEmitter.onSaveEvent();

            this.route.navigate(['/profile/listAddress']);
          });
        },
        error: (error: any) => {
          this.toastService.showErrorToast(error.error.errorMessage, 'close');
          this._ngZone.run(() => {
            this.eventEmitter.onSaveEvent();
            this.route.navigate(['/profile/listAddress']);
          });
        },
      });
    }
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

  // getLocation(): void {
  //   if (navigator.geolocation) {
  //     navigator.geolocation.getCurrentPosition(
  //       (position) => {
  //         this.latitude = position.coords.latitude;
  //         this.longitude = position.coords.longitude;
  //         this.tempName = 0;
  //       },
  //       (error) => console.error(error)
  //     );
  //   } else {
  //     console.error('Geolocation is not supported by this browser.');
  //   }
  //   if (this.tempName == 0) console.log('latitude', this.latitude);
  // }
}
