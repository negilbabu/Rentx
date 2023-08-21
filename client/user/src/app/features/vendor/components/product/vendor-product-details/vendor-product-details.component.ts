import { Component, NgZone, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators,
  FormBuilder,
  FormArray,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxImageCompressService } from 'ngx-image-compress';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { ProductService } from '../../../services/product.service';
import { firebaseStorage } from 'src/app/shared/config/firebase.config';

@Component({
  selector: 'app-vendor-product-details',
  templateUrl: './vendor-product-details.component.html',
  styleUrls: ['./vendor-product-details.component.css'],
})
export class VendorProductDetailsComponent implements OnInit {
  form: FormGroup = this.formBuilder.group({
    keyValues: this.formBuilder.array([]),
  });
  specifications: { [key: string]: string } = {};
  productId: any;
  productDetails: any;
  currentCategory: any;
  currentSubCategory: any;
  currentStore: any;
  coverImage: any;
  image1: any;
  image2: any;
  image3: any;
  image4: any;
  invalidImages: string[] = [];
  productImages: any = [];
  validFiles: File[] = [];
  editProductForm!: FormGroup;
  specificationForm!: FormGroup;
  formSubmitted = false;
  coverImageURL: any;
  thumbnailImageUrl: any;
  specifications2: any;
  roadRegex = new RegExp(
    /^(?!\s)[a-zA-Z0-9_\s!@#$%^&*()-+=~`{}[\]|:;"'<>,.?/]{3,20}(?<!\s)$/
  );
  descriptionRegex = new RegExp(
    /^(?!\s)[a-zA-Z0-9_\s!@#$%^&*()-+=~`{}[\]|:;"'<>,.?/]{3,50}(?<!\s)$/
  );
  stockRegex = new RegExp(/^(?!0)\d{1,6}$/);
  priceRegex = new RegExp(/^[0-9]{1,7}$/);
  epochCurrentTime: any;
  allStore: any;
  base64Images: string[] = []; // Add this property
  showCross: boolean[] = []; // Array to track the visibility of cross mark

  selectedSubcategory: any;
requiredIndicator:any;

  formData: String[] = [];
  optionalImageValidator: any;
  coverValidator: any;
  selectedFiles: File[] = [];
  isDragOver = false;
  images: any = [];
  ImageUrl: String[] = [];
  coverImageUrl: ImageData[] = [];
  selectedCover: any;
  allCategory: any;
  productData: any;
  transformedSpecificationData: any;
  imageLoader = true;
  iseditable: true | false = true;
  errorData: any;
  specificationKeyValue: any;
  files!: any[];
  inputIndex: any;
  imageKeyArray: any;
  toUploadImages:any;
  currentIndex: any;
  specIndex: any;
  constructor(
    private eventEmitter: EventEmitterService,
    private productService: ProductService,
    private toastService: ToastService,
    private _ngZone: NgZone,
    private route: Router,
    private router: ActivatedRoute,
    private formBuilder: FormBuilder,
    private imageCompress: NgxImageCompressService,
    private ngxService: NgxUiLoaderService,
    private errorCodeHandle: ApiErrorHandlerService
  ) {}

  ngOnInit() {
    this.productId = this.router.snapshot.params['id'];
    this.epochCurrentTime = Math.floor(Date.now() / 1000);
    this.editProductForm = this.formBuilder.group({
      name: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.roadRegex),
      ]),
      category: new FormControl('', [Validators.required]),
      subCategory: new FormControl('', [Validators.required]),
      store: new FormControl('', [Validators.required]),
      price: new FormControl('', [
        Validators.required,
        Validators.maxLength(20),
        Validators.pattern(this.stockRegex),
      ]),
      description: new FormControl('', [
        Validators.required,
        Validators.maxLength(50),
        Validators.pattern(this.descriptionRegex),
      ]),
      stock: new FormControl('', [
        Validators.required,
        Validators.maxLength(6),
        Validators.pattern(this.stockRegex),
      ]),    
      // specification: this.formBuilder.array([]),
    });
    this.loadAllCategory();
    this.loadStore();
    this.loadProductData();
    this.showCross = Array(this.base64Images.length).fill(false);
    
  }

  get keyValuesFormArray(): FormArray {
    return this.form.get('keyValues') as FormArray;
  }
loadStore(){
  this.productService.listAllStores().subscribe({
    next: (data: any) => {

      this.allStore = data.result;
    },
    error: () => {},
  });

}

loadAllCategory(){
  this.productService.getCategory().subscribe({
    next: (data: any) => {

      this.allCategory = data;
    
    },
    error: () => {},
  });
}
loadSubcategory(data: any) {
this.currentSubCategory=null;
  this.productService.getSubcategory(data).subscribe({
    next: (data: any) => {

      this.selectedSubcategory = data;
    },
    error: () => {},
  });
}
  addKeyValuePair() {
    const newKeyValuePair = this.formBuilder.group({
      key: new FormControl('', [
        Validators.required,
        Validators.pattern(this.roadRegex), // Add pattern validation for key field
      ]),
      value: new FormControl('',  [
        Validators.required,
        Validators.pattern(this.roadRegex), // Add pattern validation for key field
      ]),
    });

    this.keyValuesFormArray.push(newKeyValuePair);
  }

  removeKeyValuePair(index: number) {
    this.keyValuesFormArray.removeAt(index);
    this.updatesSpecification(null);
  }

  updatesSpecification(index:any){


    this.specificationKeyValue = this.keyValuesFormArray.controls.reduce(
      (acc: any, spec: any) => {
        const key = spec.get('key').value;
        const value = spec.get('value').value;
        acc[key] = value;
        // console.log(this.specificationKeyValue)
        return acc;
      },
      {}
    );
  }



resetForm(){
}
  loadProductData() {
    this.productService.getVendorProduct(this.productId).subscribe({
      next: (data: any) => {
        this.productDetails = data;
      console.log(data)
        this.currentCategory = data.subCategory.category.id;
        this.loadSubcategory(this.currentCategory);
        this.currentSubCategory = data.subCategory.id;
        this.specificationKeyValue=data.specification
        this.specifications2=data.specification;
        
        this.currentStore = data.store.id;
        this.coverImage = data.coverImage;
        this.image1 = data.image1;
        this.image2 = data.image2;
        this.image3 = data.image3;
        this.image4 = data.image4;
        this.productImages = {
          0: data.image1,
          1: data.image2,
          2: data.image3,
          3: data.image4,
        };

        const keyValuesArray = this.form.get('keyValues') as FormArray;
        keyValuesArray.clear();

        Object.keys(this.specifications2).forEach((key) => {
          keyValuesArray.push(
            this.formBuilder.group({
              key: key,
              value: this.specifications2[key],
            })
          );
        });
      },
      error: () => {},
    });
  }
  
  toggleEditOrView(iseditable: true | false): true | false {
    return this.iseditable === true ? false : true;

  }
  editHandle() {
      this.iseditable = this.toggleEditOrView(this.iseditable);
      
      if (this.iseditable == true) {
        this.editProductForm.reset;
        this.loadProductData();
        this.editProductForm.patchValue(this.productDetails);
      }
    }


  handleProductImageChange(event: any, position: number) {
    const newFiles = Array.from(event.target.files) as File[];
// Validation starts
    if (!this.validateImageSize(event.target.files[0])) {
      this.coverValidator = false;
      this.toastService.showErrorToast(
        'Image size should not exceed 2MB.',
        'close'
      );
      this.clearImageInput(event);
      return;
    }

    // Validate image format
    if (!this.validateImageFormat(event.target.files[0])) {
      this.coverValidator = false;
      this.toastService.showErrorToast(
        'Image should be in PNG or JPG format.',
        'close'
      );
      this.clearImageInput(event);
      return;
    }

    // Validate image ratio
    this.validateImageRatio(event.target.files[0])
      .then((isValidRatio) => {
        if (!isValidRatio) {
          this.coverValidator = false;
          this.toastService.showErrorToast(
            'Image exceeds the maximum allowed resolution.',
            'close'
          );
          this.clearImageInput(event);
        } else {
          this.coverValidator = true;

        }
      })
      .catch(() => {
        this.coverValidator = false;
        this.toastService.showErrorToast(
          'Error occurred while validating the image ratio.',
          'close'
        );
        this.clearImageInput(event);
      });
  // Ensure that the position is within the valid range (0 to 3)
  if (position >= 0 && position <= 3) {
    this.files = this.files || [];

    // If the position is already occupied, replace the file; otherwise, insert at the specified position.
    this.files[position] = newFiles[0];

    // Limit the array to a maximum of 4 files
    this.files = this.files.slice(0, 4);
  }


  this.onFilesSelected(this.files,position)

}
handleCoverFileChange(event: any) {
  const file = event.target.files[0];

  // Validate image size
  if (!this.validateImageSize(file)) {
    this.coverValidator = false;
    this.toastService.showErrorToast('Image size should not exceed 2MB.', 'close');
    this.clearImageInput(event);
    return;
  }

  // Validate image format
  if (!this.validateImageFormat(file)) {
    this.coverValidator = false;
    this.toastService.showErrorToast(
      'Image should be in PNG or JPG format.',
      'close'
    );
    this.clearImageInput(event);
    return;
  }

  // Validate image ratio
  this.validateImageRatio(file)
    .then((isValidRatio) => {
      if (!isValidRatio) {
        this.coverValidator = false;
        this.toastService.showErrorToast(
          'Image exceeds the maximum allowed resolution.',
          'close'
        );
        this.clearImageInput(event);
      } else {
        const imageUrl = URL.createObjectURL(file);
        this.coverValidator = true;
        this.selectedCover = event;

        // Display the image in the <img> tag
        const imgElement = document.getElementById(
          'selectedImage'
        ) as HTMLImageElement;
        imgElement.src = imageUrl;
      }
    })
    .catch(() => {
      this.coverValidator = false;
      this.toastService.showErrorToast(
        'Error occurred while validating the image ratio.',
        'close'
      );
      this.clearImageInput(event);
    });
}

onFilesSelected(files:any,index:any) {





  if (files.length > 4) {
    const errorMessage = 'You can only select up to 4 images.';
    this.toastService.showErrorToast(errorMessage, 'close');
    this.clearMultiImage(files);
    return;
  }

  this.selectedFiles = files;
  this.validateSelectedImages(files,index);

}

clearMultiImage(event: any) {
  event.target.value = null;
}



validateSelectedImages(event: any, index: any) {
  const maxSizeInBytes = 2 * 1024 * 1024; // 2 MB
  const maxResolutionWidth = 1280;
  const maxResolutionHeight = 1920;
  const allowedExtensions = ['jpg', 'png'];
  const validFiles: File[] = [];
  const invalidImages: string[] = [];

  for (let i = 0; i < this.selectedFiles.length; i++) {
    const file = this.selectedFiles[i];
    if (file) {
      const fileSize = file.size;
      const fileExtension: string | undefined = file.name.split('.').pop();
      const image = new Image();

      // Check file size
      if (fileSize > maxSizeInBytes) {
        invalidImages.push(file.name);
        continue;
      }

      // Check file extension
      if (
        !fileExtension ||
        !allowedExtensions.includes(fileExtension.toLowerCase())
      ) {
        invalidImages.push(file.name);
        continue;
      }

      // Check image resolution
      image.onload = () => {
        const width = image.width;
        const height = image.height;

        if (width > maxResolutionWidth || height > maxResolutionHeight) {
          invalidImages.push(file.name);
        } else {
          validFiles.push(file);
        }

        // Load selected files after all validations are complete
        if (i === this.selectedFiles.length - 1) {
          this.updateSelectedFiles(validFiles, invalidImages, index);
        }
      };

      image.src = URL.createObjectURL(file);
    }
  }
}

updateSelectedFiles(validFiles: File[], invalidImages: string[], index: any) {
  if (validFiles.length > 0) {
    if (validFiles.length <= 4 - index) {
      // Insert validFiles into selectedFiles array starting at the given index

  
      this.loadSelectedFiles(index)
    } else {
    }
  }

  if (invalidImages.length > 0) {
    // Show error message or alert with the list of invalid images
    const errorMessage = `Selected image is invalid:\n${invalidImages.join('\n')}`;
    this.toastService.showErrorToast(errorMessage, 'close');
    this.selectedFiles.splice(index, 1);
this.removeNullAndStoreIndexes()
  }
}

private loadSelectedFiles(index:any) {
  // alert('index is '+index);
    if (this.selectedFiles.length > 0) {
      const file = this.selectedFiles[index];
      const reader = new FileReader();
reader.onload = (event: any) => {

      const imageUrl = event.target.result;
      // alert(imageUrl)
if(index==0){this.image1=imageUrl}
else if(index==1){this.image2=imageUrl}
else if(index==2){this.image3=imageUrl}
else if(index==3){this.image4=imageUrl}
      };
      reader.readAsDataURL(file);
    }
  }


removeFileAtIndex( index: number) {
  if (index >= 0 && index < this.selectedFiles.length) {
    this.selectedFiles.splice(index, 1);
    
  }

  if(index==0){this.image1=null}
    if(index==1){this.image2=null;
      this.productDetails.image2=null;}
    else if(index==2){this.image3=null;
      this.productDetails.image3=null;}
    else if(index==3){this.image4=null;
      this.productDetails.image4=null;}
}


removeNullAndStoreIndexes() {
if(this.selectedFiles.length==0)
{

    const finalData = {
     
      productId: this.productId,
      image1: this.productDetails.image1,
      image2:this.productDetails.image2,
      image3:this.productDetails.image3,
      image4:this.productDetails.image4,
    };

    this.images=finalData;
    this.addOptionalImages();
}
else{
  this.imageKeyArray = [];
  this.toUploadImages = this.selectedFiles.filter((file) => file !== null);
  this.selectedFiles = this.selectedFiles.filter((file, index) => {
    if (file !== null) {
      this.imageKeyArray.push(index+1);
      this.uploadImages();
      return true; // Keep the non-null file in the array
      
    }
  else{
    return false;
  }
     // Remove the null file from the array
  });
}
}
iconColorChange() {
  let myStyles = {
    color: this.iseditable ? 'black' : 'blue',
  };
  return myStyles;
}




  async uploadImages() {

  if (!this.toUploadImages || this.toUploadImages.length === 0) {
    this.ngxService.stop();
    this.route.navigateByUrl('/vendor/products')
    return; // No files to upload
  }

  const totalFiles = this.toUploadImages.length;
  const imagesObject: any = {};
  const imageKeyArray = this.imageKeyArray; 

  try {
    for (let i = 0; i < totalFiles; i++) {
      const file = this.toUploadImages[i];

      // Check if the file is null before proceeding
      if (file === null) {
        continue;
      }

      const fileRef = firebaseStorage.ref().child(`product-images/${file.name}`);
      const snapshot = await fileRef.put(file);
      const key = `image${imageKeyArray[i]}`;
      const downloadURL = await snapshot.ref.getDownloadURL();
      imagesObject[key] = downloadURL;
    }

    console.log('multiple images uploaded successfully.');
    // All images have been uploaded, construct the final data
    const finalData = {
      productId: this.productId,
      ...imagesObject,
    };
    this.addDefaultImage1(finalData);

    // Now finalData will have image1 added with the default value if it was missing

    this.images = finalData;
    this.ImageUrl.push(imagesObject);

    // Call the function to add optional images
    this.addOptionalImages();
  } catch (error) {
    console.error('Image upload error:', error);
  } finally {
    this.ngxService.stop();
  }
}

 addDefaultImage1(finalData: any) {
  if (!finalData.hasOwnProperty('image1')) {
    finalData.image1 = this.productDetails.image1;
  }
  if (!finalData.hasOwnProperty('image2')) {
    finalData.image2 = this.productDetails.image2;
  }
  if (!finalData.hasOwnProperty('image3')) {
    finalData.image3 = this.productDetails.image3;
  }
  if (!finalData.hasOwnProperty('image4')) {
    finalData.image4 = this.productDetails.image4;
  }
}

addOptionalImages() {
  if(this.selectedFiles==null){
    this.ngxService.stop();
  }
  this.productService.updateImages(this.images).subscribe({
    next: (data: any) => {
      this._ngZone.run(() => {
        this.ngxService.stop();
        this.eventEmitter.onSaveEvent();
        this.route.navigate(['/vendor/products']);
      });
      this.toastService.showSucessToast('Product updated sucessfully', 'close');
      
    },
    error: (error: any) => {
      this.ngxService.stop();
      this.errorData = this.errorCodeHandle.getErrorMessage(
        error.error.errorCode
      );
      if (this.errorData === '007') {
        this.route.navigateByUrl('/error');
      } else {
        this.toastService.showErrorToast(this.errorData, 'close');
      }
    },
  });
}



coverSelect(event: any) {
  const file = event.target.files[0];

  // Validate image size
  if (!this.validateImageSize(file)) {
    this.coverValidator = false;
    this.toastService.showErrorToast(
      'Image size should not exceed 2MB.',
      'close'
    );
    this.clearImageInput(event);
    return;
  }

  // Validate image format
  if (!this.validateImageFormat(file)) {
    this.coverValidator = false;
    this.toastService.showErrorToast(
      'Image should be in PNG or JPG format.',
      'close'
    );
    this.clearImageInput(event);
    return;
  }

  // Validate image ratio
  this.validateImageRatio(file)
    .then((isValidRatio) => {
      if (!isValidRatio) {
        this.coverValidator = false;
        this.toastService.showErrorToast(
          'Image exceeds the maximum allowed resolution.',
          'close'
        );
        this.clearImageInput(event);
      } else {
        this.coverValidator = true;
        this.selectedCover = event;

      }
    })
    .catch(() => {
      this.coverValidator = false;
      this.toastService.showErrorToast(
        'Error occurred while validating the image ratio.',
        'close'
      );
      this.clearImageInput(event);
    });
}

validateImageSize(file: File): boolean {
  const maxSizeInBytes = 2 * 1024 * 1024; // 2MB
  return file.size <= maxSizeInBytes;
}

validateImageFormat(file: File): boolean {
  const allowedFormats = ['image/png', 'image/jpeg'];
  return allowedFormats.includes(file.type);
}


validateImageRatio(file: File): Promise<boolean> {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.onload = () => {
      const width = img.width;
      const height = img.height;
      const maxWidth = 1280;
      const maxHeight = 1920;
      resolve(width <= maxWidth && height <= maxHeight);
    };
    img.onerror = () => {
      reject();
    };
    img.src = URL.createObjectURL(file);
   
  });
}

clearImageInput(event: any) {
  event.target.value = null;
}


updateProduct(){
const formValue = this.editProductForm.value;
let coverImage  
let thumbnail
if(this.coverImageURL){
coverImage = this.coverImageURL;
thumbnail = this.thumbnailImageUrl;
}
else{
coverImage= this.coverImage;
thumbnail=this.coverImage;
}


const formData = {
...formValue,
specification: this.specificationKeyValue,
coverImage,
thumbnail,
};

this.formData.push(formData);
this.formSubmitted = true;
if (this.editProductForm.valid) {
this.productService.editProductDetails(formData,this.productId).subscribe({
  next: (data: any) => {
    this._ngZone.run(() => {
this.removeNullAndStoreIndexes();


    })


    this.productId = data.id;
  },
  error: (error: any) => {

    this._ngZone.run(() => {
      
    });
    this.errorData = this.errorCodeHandle.getErrorMessage(
      error.error.errorCode
    );
    if (this.errorData === '007') {
      this.route.navigateByUrl('/error');
    } else {
      this.toastService.showErrorToast(this.errorData, 'close');
    }
  },
});
}
}


onCoverCompress() {
  if(this.editProductForm.invalid || this.form.invalid){
    this.formSubmitted=true
  }
 if(this.selectedCover==null && this.editProductForm.valid && this.form.valid)
 {
this.updateProduct();
 }

if ( this.selectedCover!=null && this.editProductForm.valid && this.form.valid) {

      this.ngxService.start();
      const file: File = this.selectedCover.target.files[0];
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const img = new Image();
        img.src = e.target.result;

        img.onload = () => {
          const canvas = document.createElement('canvas');
          const ctx = canvas.getContext('2d');

          const maxWidth = 800; // Set the maximum width for the compressed image
          const maxHeight = 600; // Set the maximum height for the compressed image

          let width = img.width;
          let height = img.height;

          // Calculate the new width and height while maintaining the aspect ratio
          if (width > height) {
            if (width > maxWidth) {
              height *= maxWidth / width;
              width = maxWidth;
            }
          } else {
            if (height > maxHeight) {
              width *= maxHeight / height;
              height = maxHeight;
            }
          }

          // Set the canvas dimensions to the new width and height
          canvas.width = width;
          canvas.height = height;

          // Draw the image on the canvas with the new dimensions
          ctx?.drawImage(img, 0, 0, width, height);

          // Get the compressed image data as a Blob
          canvas.toBlob(
            (blob) => {
              if (blob) {
                // Create a reference for the original image in Firebase Storage
                const originalFileRef = firebaseStorage
                  .ref()
                  .child(`product-images/${file.name}`);
                const originalUploadTask = originalFileRef.put(file);

                originalUploadTask
                  .then((originalSnapshot) => {
                    // Original image upload successful
                    const compressedFileName = `compressed_${file.name}`;

                    // Create a reference for the compressed image in Firebase Storage
                    const compressedFileRef = firebaseStorage
                      .ref()
                      .child(`product-images/${compressedFileName}`);
                    const compressedUploadTask = compressedFileRef.put(blob);

                    compressedUploadTask
                      .then((compressedSnapshot) => {
                        // Compressed image upload successful
                        Promise.all([
                          originalSnapshot.ref.getDownloadURL(),
                          compressedSnapshot.ref.getDownloadURL(),
                        ]).then((downloadURLs) => {
                          
                          this.coverImageURL = downloadURLs[0];
                          this.thumbnailImageUrl = downloadURLs[1];
                          this.onproductDetailsSubmit();
                        });
                      })
                      .catch((compressedError) => {
                        // Compressed image upload failed
                        console.error(
                          'Compressed image upload error:',
                          compressedError
                        );
                      });
                  })
                  .catch((originalError) => {
                    // Original image upload failed
                    console.error(
                      'Original image upload error:',
                      originalError
                    );
                  });
              }
            },
            'image/jpeg',
            1.0
          );
        };
      };

      reader.readAsDataURL(file);
    }
  }
  onproductDetailsSubmit(){
this.specificationKeyValue = this.keyValuesFormArray.controls.reduce(
  (acc: any, spec: any) => {
    const key = spec.get('key').value;
    const value = spec.get('value').value;
    acc[key] = value;
    return acc;
  },
  {}
);


this.updateProduct()
}

}