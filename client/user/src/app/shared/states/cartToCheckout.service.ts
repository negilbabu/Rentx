import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class cartToCheckoutService {
  private cartToCheckoutSubject = new BehaviorSubject<number>(0);
  checkoutCart$ = this.cartToCheckoutSubject.asObservable();

  cartToCheckout(padding: any) {
    this.cartToCheckoutSubject.next(padding);
  }
}
