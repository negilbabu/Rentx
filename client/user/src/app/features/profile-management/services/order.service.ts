import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Store } from 'src/app/models/store.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiURL = environment.apiUrl;
  constructor(private httpClient: HttpClient) {}

  getOrderlist(): Observable<Store> {
    
    return this.httpClient.get<Store>(
      `${this.apiURL}/user/order/history?size=20000000`
    );
  }
  getOrderDetail(id:any): Observable<Store> {
    return this.httpClient.get<Store>(
      `${this.apiURL}/user/order/history/detailedView/${id}`
    );
  }


  cancelOrder(id:any): Observable<Store> {
    return this.httpClient.put<Store>(
      `${this.apiURL}/user/order/cancel/${id}`,null
    );
  }
  }


