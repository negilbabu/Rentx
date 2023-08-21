import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EventEmitterService {
 
  constructor() {}
  private subject = new Subject<any>();
  private padding = new Subject<any>();
  private reloadSidebar = new Subject<any>();
  private filterDates=new Subject<any>();
  private reloadSidebarSubject = new Subject<any>();

  onReloadSidebar() {
    return this.reloadSidebarSubject.asObservable();
  }
  getStartAndEndDate(startDate: any, endDate: any) {
    const dateRange = {
      startDate: startDate,
      endDate: endDate
    };
    this.filterDates.next(dateRange);
  }
  
  onFilterDates(): Observable<any> {
    return this.filterDates.asObservable();
  }

  reloadSidebarEvent() {
    this.reloadSidebarSubject.next(1);
  }

  getsidebarEvent(padding: number) {
    this.padding.next(padding);
  }
  sidebarEvent$() {
    return this.padding.asObservable();
  }
  onSaveEvent() {
    this.subject.next(1);
  }
  getonSaveEvent(): Observable<any> {
    return this.subject.asObservable();
  }
  onDeleteEvent() {
    this.subject.next(1);
  }
  getonDeleteEvent(): Observable<any> {
    return this.subject.asObservable();
  }
  onEditEvent() {
    this.subject.next(1);
  }

  getonEditEvent(): Observable<any> {
    return this.subject.asObservable();
  }

  onLoadEvent() {
    this.subject.next(1);
  }
  getonLoadEvent(): Observable<any> {
    return this.subject.asObservable();
  }
}
