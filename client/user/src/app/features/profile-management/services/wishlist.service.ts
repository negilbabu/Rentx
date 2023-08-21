import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Store } from 'src/app/models/store.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WishlistService {
  private apiURL = environment.apiUrl;
  constructor(private httpClient: HttpClient) {}

  getWishlist(): Observable<Store> {
    
    return this.httpClient.get<Store>(
      `${this.apiURL}/user/wishlist/list?size=20000000`
    );
  }


  addToWishlist(id:any): Observable<Store> {
    
    return this.httpClient.post<Store>(
      `${this.apiURL}/user/wishlist/add/${id}`,null
    );
  }

  removeFromWishlist(productId:any): Observable<Store> {
    
    return this.httpClient.put<Store>(
      `${this.apiURL}/user/wishlist/remove/${productId}`,null
    );
  }


}