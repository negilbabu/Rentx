import { HttpClient } from '@angular/common/http';
import { Injectable, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {
  getPaymentDetail(paymentId: number) {
    return this.httpClient.get(
      `${this.apiURL}/admin/paymentMethod/detail/${paymentId}`
    );
  }
  updatePayment(paramBody: any, paymentId: any): Observable<any> {
    return this.httpClient.put(
      `${this.apiURL}/admin/paymentMethod/edit/${paymentId}`,
      paramBody
    );
  }

  deletePayment(paymentId: any): Observable<any> {
    console.log(`${this.apiURL}/admin/paymentMethod/${paymentId}`, null);

    return this.httpClient.put(
      `${this.apiURL}/admin/paymentMethod/${paymentId}`,
      null
    );
  }

  // deletePayment() {
  //   return this.httpClient.put(`${this.apiURL}/admin/paymentMethod/list`);
  // }
  getPayment(): Observable<any> {
    return this.httpClient.get(`${this.apiURL}/admin/paymentMethod/list`);
  }
  private apiURL = environment.apiUrl;
  addPayment(data: any): Observable<any> {
    console.log(data);
    return this.httpClient.post(`${this.apiURL}/admin/paymentMethod/add`, data);
  }
  constructor(
    private httpClient: HttpClient,
    private _ngZone: NgZone,
    private router: Router
  ) {}
}
