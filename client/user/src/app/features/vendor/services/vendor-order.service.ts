import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Store } from 'src/app/models/store.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VendorOrderService {
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





getOrderHistoryList(
  searchData: string = '',
  sortField: string = 'updatedAt',
  sortDirection: string = 'ASC',
  pageNo: number = 1,
  pageSize: number = 7,
): Observable<any> {
const url = `${this.apiURL}/vendor/order/history?searchData=${searchData}&page=${pageNo}&size=7&sort=${sortField}&direction=${sortDirection}`
console.log(url)

  return this.httpClient.get<any>(
    `${this.apiURL}/vendor/order/history?searchData=${searchData}&page=${pageNo}&size=7&sort=${sortField}&direction=${sortDirection}`
  );
}


getOrderDetail(id:any): Observable<Store> {
  return this.httpClient.get<Store>(
    `${this.apiURL}/vendor/order/history/detailedView/${id}`
  );
}


cancelOrder(id:any): Observable<Store> {
  return this.httpClient.put<Store>(
    `${this.apiURL}/vendor/order/cancel/${id}`,null
  );
}
  
}
