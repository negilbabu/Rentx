import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SidebarPaddingService {
  private sidebarPaddingSubject = new BehaviorSubject<any>(
    localStorage.getItem('token')
  );
  padding$ = this.sidebarPaddingSubject.asObservable();

  updateSidebarPadding(padding: any) {
    this.sidebarPaddingSubject.next(padding);
  }
}
