import { Component, NgZone, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormArray, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NgxImageCompressService } from 'ngx-image-compress';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ProductService } from '../features/vendor/services/product.service';
import { EventEmitterService } from '../services/event-emitter.service';
import { ToastService } from '../shared/services/toast.service';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent implements OnInit {

  invalidImages: string[] = [];
  productImages:any=[];
  validFiles: File[] = [];
  productDetails:any
  editProductForm!: FormGroup;
  formSubmitted = false;
  coverImageURL:any;
  thumbnailImageUrl:any;
  formData:String[]=[];
 optionalImageValidator:any;
 stockRegex = new RegExp(/^(?!0)\d{1,6}$/);
  priceRegex = new RegExp(/^[0-9]{1,7}$/);
  coverValidator:any;
  descriptionRegex = new RegExp(/^(?!\s)[a-zA-Z0-9_\s!@#$%^&*()-+=~`{}[\]|:;"'<>,.?/]{3,50}(?<!\s)$/);
  roadRegex = new RegExp(/^(?!\s)[a-zA-Z0-9_\s!@#$%^&*()-+=~`{}[\]|:;"'<>,.?/]{3,20}(?<!\s)$/);
  selectedFiles: File[] = [];
  isDragOver = false;
  images:String[]=[];
  ImageUrl:String[]=[];
  coverImageUrl: ImageData[] = [];
  selectedCover:any;
  base64Images: string[] = []; // Add this property
  showCross: boolean[] = []; // Array to track the visibility of cross mark
  productId: any;
  allCategory:any;
  allStore:any;
  currentCategory:any;
  currentStore:any;
  currentSubCategory:any;
  selectedSubcategory:any;
  image1:any;
  image2:any;
  image4:any;
  image3:any;
  productData:any;
  coverImage:any;
  transformedSpecificationData:any;
  epochCurrentTime:any;
  imageLoader=false;
  iseditable: true | false =true;



  constructor(
    private eventEmitter: EventEmitterService,
    private productService: ProductService,
    private toastService: ToastService,
    private _ngZone: NgZone,
    private route: Router,
    private formBuilder: FormBuilder,
    private imageCompress: NgxImageCompressService,
    private ngxService: NgxUiLoaderService,
    private router: ActivatedRoute,
  ) {
    
  }

  ngOnInit(): void {
    setTimeout(() => {this.imageLoader=true}, 4000);
    this.productId = this.router.snapshot.params['id'];
    this.epochCurrentTime= Math.floor(Date.now() / 1000); // Divide by 1000 to convert from milliseconds to seconds
    console.log(this.epochCurrentTime); 
    this.coverValidator=true;
    this.optionalImageValidator=true;
    this.productService.getCategory().subscribe({
      next: (data: any) => {
        console.log("Category = ",data);
        this.allCategory =data;
        this.addSpecification();
      },
      error: () => {},
    });

    this.productService.listAllStores().subscribe({
      next: (data: any) => {
        console.log("Store =",data);
        this.allStore =data.result;

      },
      error: () => {},
    });


    this.productService.getVendorProduct(this.productId).subscribe({
      next: (data: any) => {
        console.log("product =",data);
this.productDetails=data;
this.currentCategory=data.subCategory.category.id;
this.currentSubCategory=data.subCategory.id;
this.currentStore=data.store.id;
this.coverImage=data.coverImage;
this.image1=data.image1;
this.image2=data.image2;
this.image3=data.image3;
this.image4=data.image4;
this.productImages= {
  0:data.image1,
  1:data.image2,
  2:data.image3,
  3:data.image4,
};    
console.log(this.productImages)
this.transformedSpecificationData = Object.entries(data.specification).map(([key, value]) => ({
  key,
  value
}));
console.log("sssssssssaaaa",this.transformedSpecificationData)

      },
      error: () => {},
    });

// Initialize showCross array with false values
this.showCross = Array(this.base64Images.length).fill(false);
    this.editProductForm = this.formBuilder.group({
    
      specification: this.formBuilder.array([])
    });
  }
  get specificationControls(): FormArray {
    return this.editProductForm.get('specification') as FormArray;
  }
toggleEditOrView(iseditable:true | false): true | false{
  return this.iseditable === true ? false : true;   
}
editHandle(){
  this.iseditable = this.toggleEditOrView(this.iseditable);

}

  clearSpecifications(): void {
    while (this.specificationControls.length !== 0) {
      this.specificationControls.removeAt(0);
    }
  }
 
  populateSpecifications(): void {
    // Clear existing specifications
    this.clearSpecifications();
  
    // Get the specification FormArray
    const specificationArray = this.editProductForm.get('specification') as FormArray;
  
    // Loop through the transformedSpecificationData and populate the form array
    this.transformedSpecificationData.forEach((item: { key: string, value: string }) => {
      const specificationGroup = this.formBuilder.group({
        key: [item.key, [
          Validators.required,
          Validators.maxLength(20),
          Validators.pattern(this.roadRegex),
        ]],
        value: [item.value, [
          Validators.required,
          Validators.maxLength(20),
          Validators.pattern(this.roadRegex),
        ]]
      });
  
      specificationArray.push(specificationGroup);
    });
  }
  
  
  
  addSpecification(key: string = '', value: string = ''): void {
    const specificationGroup = this.formBuilder.group({
      key: [key, [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]],
      value: [value, [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]]
    });
  
    this.specificationControls.push(specificationGroup);
  }
  
  removeSpecification(index: number): void {
    this.specificationControls.removeAt(index);
  }
 




}  
  
  
  

