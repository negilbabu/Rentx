import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Store } from 'src/app/models/store.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CartServicesService {
  private apiURL = environment.apiUrl;
  constructor(private httpClient: HttpClient) {}

  // addStores
  dateFormatted(date:any){
    let formattedDate = new Date(date);
    formattedDate.setDate(formattedDate.getDate() + 1); // Add one more day
     return formattedDate = date?.toISOString().substring(0, 10);
    
  }

  addToCart(
    id :any,
    quantity:any = 1,
    startDate = this.dateFormatted(new Date()),
    endDate = this.dateFormatted(new Date()),
  ): Observable<any> {
  const cartBody ={
   productId: id,
   quantity:quantity,
   startDate:startDate,
   endDate:endDate
  }
    return this.httpClient.post(
      `${this.apiURL}/user/cart/add`,cartBody
    
    );
  }




getCartData(){
  return this.httpClient.get(
    `${this.apiURL}/user/cart/list?size=20000000&order=desc`
  );
}

removeProduct(id:any){
  return this.httpClient.put(
    `${this.apiURL}/user/cart/remove/${id}`,null 
  );
}

editProductFromCart(id:any,quantity:any,startDate:any,endDate:any){
  return this.httpClient.put(
    `${this.apiURL}/user/cart/${id}?quantity=${quantity}&endDate=${endDate}&startDate=${startDate}`,null
  );
}

}