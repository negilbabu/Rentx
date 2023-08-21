import {
  Component,
  ElementRef,
  NgZone,
  OnInit,
  ViewChild,
} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { StoreService } from '../../../services/store.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import * as mapboxgl from 'mapbox-gl';
import * as MapboxGeocoder from '@mapbox/mapbox-gl-geocoder';
import { environment } from 'src/environments/environment';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';

@Component({
  selector: 'app-add-store',
  templateUrl: './add-store.component.html',
  styleUrls: ['./add-store.component.css'],
})
export class AddStoreComponent implements OnInit {
  storeDetailsForm!: FormGroup;
  formSubmitted = false;
  buttonDisabler = false;
  isCityValid = false;
  errorData: any;

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
  constructor(
    private eventEmitter: EventEmitterService,
    private storeService: StoreService,
    private toastService: ToastService,
    private _ngZone: NgZone,
    private route: Router,
    private ngxService: NgxUiLoaderService,
    private errorCodeHandle:ApiErrorHandlerService

  ) {}

  ngOnInit(): void {
    this.storeDetailsForm = new FormGroup({
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

    const geocoder = new (MapboxGeocoder as any)({
      accessToken: (MapboxGeocoder as any).accessToken,
      types: 'place',
      placeholder: 'Search for a location',
      countries: 'IN',
      container: 'search-box',

      bbox: [68.1, 6.5, 97.4, 35.5], // Bounding box for India
    });

    geocoder.addTo('#search-box');

    geocoder.on('result', (e: any) => {
      this.isCityValid = true;
      // Handle the selected location result
      console.log(e.result); // Display the details of the selected location
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

      console.log(this.formattedData);
    });

    geocoder.on('clear', () => {
      this.formattedData.state = '';
      this.formattedData.district = '';
      this.formattedData.country = '';
    });
  }
  // return an object with a random latitude and longitude
  setLatLong() {
    // const lat = Math.random() * 180 - 90;
    // const long = Math.random() * 360 - 180;
    return {
      city: this.formattedData.city,
      lattitude: this.formattedData.latitude,
      longitude: this.formattedData.longitude,
    };
  }

  // validateData(data: any) {
  //   return this.trimObjectStrings(this.storeDetailsForm.value);
  // }
  onStoreDetailsSubmit() {
    this.formSubmitted = true;

    this.storeDetailsForm.get('city')?.setValue(this.formattedData.city);

    if (this.storeDetailsForm.valid) {
      this.buttonDisabler = true;

      // this.ngxService.start();
      const storeDetailsData = {
        ...this.storeDetailsForm.value,
        ...this.setLatLong(),
      };
      console.log(storeDetailsData);

      this.storeService.addStoreDetails(storeDetailsData).subscribe({
        next: (data: any) => {
          this.toastService.showSucessToast('Store added sucessfully', 'close');
          this._ngZone.run(() => {
            this.eventEmitter.onSaveEvent();
            this.route.navigate(['/vendor/stores']);
          });
        },
        error: (error:any) => {
          this.ngxService.stop();
      this.buttonDisabler = false;

          this._ngZone.run(() => {
            this.route.navigate(['/vendor/addStore']);
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
}
