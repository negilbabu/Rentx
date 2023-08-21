import { Component, ElementRef, OnInit, ViewChild,Input } from '@angular/core';
import { UserProductService } from '../../services/user-product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pos-home',
  templateUrl: './pos-home.component.html',
  styleUrls: ['./pos-home.component.css'],
})
export class PosHomeComponent implements OnInit {
  @Input() latitudeParam!:any;
  @Input() longitudeParam!:any;

  categoryData: any=[
    {
      "id": 0,
      "name": "Test1",
      "coverImage": "/assets/banners/wed.jpg",
      "createdAt": "2023-04-28T14:26:52.813",
      "updatedAt": null,
      "status": 0
    },
    {
      "id": 0,
      "name": "Test2",
      "coverImage": "/assets/banners/wed.jpg",
      "createdAt": "2023-05-08T15:54:30.155",
      "updatedAt": null,
      "status": 0
    },
    {
      "id": 0,
      "name": "Test3",
      "coverImage": "/assets/banners/wed.jpg",
      "createdAt": "2023-05-08T15:54:34.596",
      "updatedAt": null,
      "status": 0
    },
    {
      "id": 0,
      "name": "Test4",
      "coverImage": "/assets/banners/wed.jpg",
      "createdAt": "2023-05-26T10:13:07.482124",
      "updatedAt": "2023-05-26T10:13:07.482157",
      "status": 0
    },
    {
      "id": 0,
      "name": "Test5",
      "coverImage": "/assets/banners/wed.jpg",
      "createdAt": "2023-06-06T11:32:44.845592",
      "updatedAt": "2023-06-06T11:32:44.845629",
      "status": 0
    },
    {
      "id": 0,
      "name": "Test6",
      "coverImage": "/assets/banners/wed.jpg",
      "createdAt": "2023-05-26T10:13:07.482124",
      "updatedAt": "2023-05-26T10:13:07.482157",
      "status": 0
    },
    {
      "id": 0,
      "name": "Test7",
      "coverImage": "/assets/banners/wed.jpg",
      "createdAt": "2023-06-06T11:32:44.845592",
      "updatedAt": "2023-06-06T11:32:44.845629",
      "status": 0
    }
  ]
  constructor(private categoryService:UserProductService, private route:Router) {}

  ngOnInit(): void {

this.getCategoryData();


    
  }

  getCategoryData(){
    this.categoryService.getTop7Categories().subscribe((res: any) => {
    
      this.categoryData = res;
   

      console.log(res);

    });
  }

callCategory(category:any){


this.route.navigateByUrl('/products?page=1&categoryFilterParams='+category.name+':'+category.id)
}


}
