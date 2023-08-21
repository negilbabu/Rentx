import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class DataFormatingService {
  dateFormatted(date: any) {
    if (date == null) {
      return null;
    }
    let formattedDate = new Date(date);
    formattedDate.setDate(formattedDate.getDate() + 1); // Add one more day
    return formattedDate.toISOString().substring(0, 10);
  }
}
