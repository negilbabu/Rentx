import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderServiceService {
  private apiURL = environment.apiUrl;
  constructor(private httpClient: HttpClient) {}


getOrderData(cartIDs: any) {
  console.log('yeahhhh', cartIDs);
  return this.httpClient.post(
    `${this.apiURL}/user/order/summary`,
    { cartId: cartIDs }
  );
}

placeOrder(cartIDs: any,addressId:any,paymentId:any) {
 
  return this.httpClient.post(
    `${this.apiURL}/user/order/add`,
    { cartId: cartIDs,
      addressId:addressId,
      paymentId:paymentId
    }
  );
}


getPaymentMethod() {

  return this.httpClient.get(
    `${this.apiURL}/user/paymentMethod/list`,

  
  );



}


  
}
