import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import { DataFormatingService } from 'src/app/shared/services/data-formating.service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserProductService {
  private apiURL = environment.apiUrl;
  private productList: any = [];
  private lastPage = 'lastPage';
  public currentPage: any;

  constructor(
    private httpClient: HttpClient,
    private router: Router,
    private authService: AuthService,
    private dataFormatingService: DataFormatingService
  ) {}

  setlastPage(value: boolean) {
    localStorage.setItem(this.lastPage, value.toString());
  }

  // list out the products .
  listProduct(
    pageNo = 1,
    pageSize = 15,
    sortField = '',
    sortDirection = '',
    searchQuery = '',
    categoryFilter = '',
    storeFilter = '',startDate:any,endDate:any,
    latitudeFilter:any='',
    longitudeFilter:any='',


  ): Observable<any> {

    if(latitudeFilter!=''){
      sortDirection='ASC';
      
    }
    else if(latitudeFilter!=''&& sortField=="price"){
      sortField = 'updatedAt',
      sortDirection = 'DESC';
    }
    
    else if(sortField=='price'){

    }
    else if(latitudeFilter!=''&&categoryFilter!=''||storeFilter!=''){
      sortDirection='ASC';
    }
    else{
      console.log("aaaaaaaaaaaaaaaaaaaaaa",latitudeFilter);
      
      sortField = 'updatedAt',
      sortDirection = 'DESC';
    }

   
    if (this.authService.isAuthenticated()) {
      
      
      return this.httpClient
        .get<any>(
          `${this.apiURL}/user/product/list?search=${searchQuery}&page=${pageNo}&size=${pageSize}&sort=${sortField}&order=${sortDirection}&category=${categoryFilter}&store=${storeFilter}&startDate=${startDate}&endDate=${endDate}&latitude=${latitudeFilter}&longitude=${longitudeFilter}`
        )
        .pipe(
          tap((res) => {
            this.productList = res.data;
            this.setlastPage(res.lastPage);
          })
        );
    }
    return this.httpClient
      .get<any>(
        `${this.apiURL}/unAuthorized/user/product/list?search=${searchQuery}&page=${pageNo}&size=${pageSize}&sort=${sortField}&order=${sortDirection}&category=${categoryFilter}&store=${storeFilter}&startDate=${startDate}&endDate=${endDate}&latitude=${latitudeFilter}&longitude=${longitudeFilter}`
      )
      .pipe(
        tap((res) => {
          this.productList = res.data;
          this.setlastPage(res.lastPage);
        })
      );
  }

  getProductById(id: number): Observable<any> {
    if (this.authService.isAuthenticated()) {
      return this.httpClient.get<any>(`${this.apiURL}/user/product/${id}`);
    }
    return this.httpClient.get<any>(
      `${this.apiURL}/unAuthorized/user/product/${id}`
    );
  }

  checkProductAvailability(id: number, params: any): Observable<any> {
    console.log(params, 'checkProductAvailability');
    const startDate = this.dataFormatingService.dateFormatted(params.start);
    const endDate = this.dataFormatingService.dateFormatted(params.end);
    const qty = params.qty;
    console.log(startDate, 'startDate', endDate, 'endDate', qty, 'qty');

    return this.httpClient.get<any>(
      `${this.apiURL}/unAuthorized/user/availability/${id}?startDate=${startDate}&endDate=${endDate}&quantity=${qty}`
    );
  }

  getTop7Categories(): Observable<any>{
    return this.httpClient.get<any>(
      `${this.apiURL}/unAuthorized/category/priority`
    );
  }


}
