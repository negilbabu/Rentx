import { Component, Inject, NgZone, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import {
  MatDialog,
  MatDialogRef,
  MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { CategoryService } from '../../services/category.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { firebaseStorage } from 'src/app/shared/config/firebase.config';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';

@Component({
  selector: 'app-edit-category',
  templateUrl: './edit-category.component.html',
  styleUrls: ['./edit-category.component.css'],
})
export class EditCategoryComponent implements OnInit, OnDestroy {
  formSubmitted = false;
  categories: any[] = [];
  pattern = '^[a-zA-Z0-9]+(?:[ ][a-zA-Z0-9]+)*$';
  minLength = 3;
  updateCategorySubscription!: Subscription;
  maxLength = 20;
  showErrorMessage = false;
  errorData: any;
  updateCategoryForm!: FormGroup;
  categoryName: any;
  coverImage: any;
  imageLoader = false;
  coverValidator = false;
  selectedCover: any;
  epochCurrentTime: any;
  thumbnailImageUrl: any;
  constructor(
    private eventEmitter: EventEmitterService,
    private categoryService: CategoryService,
    private route: Router,
    private dialog: MatDialog,
    private ngxService: NgxUiLoaderService,
    public toast: ToastService,
    private _ngZone: NgZone,
    private sanitizer: DomSanitizer,private errorCodeHandle: ApiErrorHandlerService,
    private dialogRef: MatDialogRef<EditCategoryComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { categoryId: number }
  ) {}
  ngOnDestroy(): void {
    this.dialog.closeAll();
    this.dialogRef.close();
  }

  ngOnInit(): void {
    this.epochCurrentTime = Math.floor(Date.now() / 1000);
    setTimeout(() => {
      this.imageLoader = true;
    }, 2000);
    this.updateCategoryForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.pattern(this.pattern),
        Validators.minLength(this.minLength),
        Validators.maxLength(this.maxLength),
      ]),
      coverImage: new FormControl(''),
    });
    this.getCategoryName(this.categoryId);
    this.updateCategoryForm.get('name')?.setValue(this.categoryName);
  }

  get categoryId(): number {
    return this.data.categoryId;
  }

  validateImageSize(file: File): boolean {
    const maxSizeInBytes = 2 * 1024 * 1024; // 2MB
    return file.size <= maxSizeInBytes;
  }
  clearImageInput(event: any) {
    event.target.value = null;
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
  handleFileChange(event: any) {
    const file = event.target.files[0];

    // Validate image size
    if (!this.validateImageSize(file)) {
      this.coverValidator = false;
      this.toast.showErrorToast('Image size should not exceed 2MB.', 'close');
      this.clearImageInput(event);
      return;
    }

    // Validate image format
    if (!this.validateImageFormat(file)) {
      this.coverValidator = false;
      this.toast.showErrorToast(
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
          this.toast.showErrorToast(
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
        this.toast.showErrorToast(
          'Error occurred while validating the image ratio.',
          'close'
        );
        this.clearImageInput(event);
      });
  }

  closeDialog() {
    if (this.updateCategoryForm) {
      this.updateCategoryForm.reset();
    }
    this.formSubmitted = false;
    this.dialog.closeAll();
    this.dialogRef.close();
  }
  handleInputChange() {
    this.showErrorMessage = false; // reset flag on input change
  }
  onSubmit() {
    this.formSubmitted = true;
    if (this.updateCategoryForm.valid) {
      this.updateCategorySubscription = this.categoryService
        .updateCategory(this.categoryId, this.updateCategoryForm.value)
        .subscribe({
          next: (result: any) => {
            this._ngZone.run(() => {
              this.dialog.closeAll();
              this.ngxService.stop();
              this.toast.showSucessToast(
                'Category updated successfully',
                'close'
              );

              this.eventEmitter.onSaveEvent();
            });
          },
          error: (error: any) => {
            this._ngZone.run(() => {
              this.ngxService.stop();
              this.showErrorMessage = true;
              this.errorData = error.error.errorMessage;
              this.errorData =
                this.errorData.charAt(0).toUpperCase() +
                this.errorData.slice(1).toLowerCase();
            });
          },

          complete: () => {},
        });
    }
  }

  getCategoryName(id: any) {
    this.categoryService.getCategoryName(id).subscribe({
      next: (result: any) => {
        this.categories = result;
        console.log(result);
        this.categoryName = result.result[0].category;
        this.coverImage = result.result[0].coverImage;
        this.updateCategoryForm.get('coverImage')?.setValue(this.coverImage);
      },
      error: (error: any) => {
        this.ngxService.stop();
        this.errorData = this.errorCodeHandle.getErrorMessage(
          error.error.errorCode
        );
        if (this.errorData === '007') {
          this.route.navigateByUrl('/error');
        } else {
          this.toast.showErrorToast(this.errorData, 'close');
        }
       },
    });
  }
  onCoverCompress() {
    if (this.selectedCover == null) {
      if (this.updateCategoryForm.invalid) {
        this.formSubmitted = true;
      } else {
        this.onSubmit();
      }
    }
    if (this.coverImage == null) {
      this.coverValidator = false;
    } else {
      this.coverValidator = true;

      this.updateCategoryForm.controls['coverImage'].setValue(this.coverImage);
      console.log(this.updateCategoryForm.value);
      if (this.updateCategoryForm.valid) {
        this.formSubmitted = true;
        const file: File = this.selectedCover.target.files[0];
        const reader = new FileReader();
        reader.onload = (e: any) => {
          const img = new Image();
          img.src = e.target.result;

          img.onload = () => {
            this.ngxService.start();
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
                  const compressedFileName = `compressedCategory_${this.epochCurrentTime}`;
                  // Create a reference for the compressed image in Firebase Storage
                  const compressedFileRef = firebaseStorage
                    .ref()
                    .child(`category-images/${compressedFileName}`);
                  const compressedUploadTask = compressedFileRef.put(blob);

                  compressedUploadTask
                    .then((compressedSnapshot) => {
                      // Compressed image upload successful
                      compressedSnapshot.ref
                        .getDownloadURL()
                        .then((downloadURL) => {
                          this.thumbnailImageUrl = downloadURL;
                          this.updateCategoryForm.controls[
                            'coverImage'
                          ].setValue(this.thumbnailImageUrl);

                          this.onSubmit();
                        });
                    })
                    .catch((compressedError) => {
                      // Compressed image upload failed
                      console.error(
                        'Compressed image upload error:',
                        compressedError
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
  }
}
