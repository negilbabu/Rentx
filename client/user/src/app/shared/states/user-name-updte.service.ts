import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserNameUpdteService {
  private userNameUpdateSubject = new BehaviorSubject<any>(localStorage.getItem('username'));
  userNameUpdate$ =this.userNameUpdateSubject.asObservable();
  name:any;
 updateUserName(username : any){
  this.userNameUpdateSubject.next(username);
 }
}
