import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {
  constructor(private httpClient: HttpClient) {}
  private apiURL = environment.apiUrl;
  viewProfile(): Observable<any> {
    return this.httpClient.get(`${this.apiURL}/user/profile/view`);
  }
  updateProfile(profileData: any) {
    return this.httpClient.put(
      `${this.apiURL}/user/profile/update`,
      profileData
    );
  }
  
}
