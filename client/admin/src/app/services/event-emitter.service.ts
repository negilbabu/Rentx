import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EventEmitterService {
  constructor() {}
  private subject = new Subject<any>();
  private padding = new Subject<any>();
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
}
