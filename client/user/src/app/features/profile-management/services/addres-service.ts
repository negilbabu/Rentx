import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Store } from 'src/app/models/store.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AddresListService {
  private apiURL = environment.apiUrl;
  constructor(private httpClient: HttpClient) {}


  getAddressList(){
    return this.httpClient.get(
      `${this.apiURL}/user/address/list`
    );
  }

  addAddress(addressDetails: any): Observable<Store> {
    return this.httpClient.post<Store>(
      `${this.apiURL}/user/address/add`,
      addressDetails
    );
  }

  editAddress(addressId: any, addressDetails:any ): Observable<Store> {
    return this.httpClient.post<Store>(
      `${this.apiURL}/user/address/edit/${addressId}`,
      addressDetails
    );
  }


  deleteAddress(addressId: any): Observable<Store> {
    return this.httpClient.put<Store>(
      `${this.apiURL}/user/address/remove/${addressId}  ` ,
      null
    );
  }

  getAddressById(addressId: any): Observable<Store> {
    
    return this.httpClient.get<Store>(
     
      `${this.apiURL}/user/address/list/${addressId}  `
    );
  }







  updateAddress(addressId: any, addressDetails:any): Observable<Store> {
    
    return this.httpClient.put<Store>(
     
      `${this.apiURL}/user/address/${addressId}`,addressDetails
    );
  }


}
