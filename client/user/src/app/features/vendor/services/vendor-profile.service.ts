import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class VendorProfileService {
  getVendorBankDetails(): Observable<any> {
    return this.httpClient.get(`${this.apiURL}/vendor/bankDetails/view`);
  }
  updateVendorBankDetails(vendorBankDetailForm:any) {
    
    return this.httpClient.put(
      `${this.apiURL}/vendor/bankDetails`,
      vendorBankDetailForm
    );
  }
  private apiURL = environment.apiUrl;
  constructor(private httpClient: HttpClient) {}
  viewProfile(): Observable<any> {
    return this.httpClient.get(`${this.apiURL}/vendor/profileDetails`);
  }
  updateProfile(profileData: any) {
    return this.httpClient.put(
      `${this.apiURL}/vendor/profile/update`,
      profileData
    );
  }
}
