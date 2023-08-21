import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { CategoryService } from '../../services/category.service';
import { DeleteCategoryComponent } from '../delete-category/delete-category.component';
import { Subscription } from 'rxjs';
import { OnDestroy } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-delete-subcategory',
  templateUrl: './delete-subcategory.component.html',
  styleUrls: ['./delete-subcategory.component.css']
})
export class DeleteSubcategoryComponent implements OnDestroy {
  categoryDeleteSubscription!: Subscription; 
  errorData: any;
  constructor(
    private httpClient: HttpClient,
    private eventEmitter: EventEmitterService,private errorCodeHandle: ApiErrorHandlerService,
    private categoryService: CategoryService,
    private dialogRef: MatDialogRef<DeleteCategoryComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { subCategoryId: number },
    public toast: ToastService,private route:Router
  ) {}

  get subCategoryId(): number {
     return this.data.subCategoryId;
  }

  onCancel() {
    this.dialogRef.close();
  }

  onDelete() {
    this.categoryDeleteSubscription =this.categoryService.deleteSubCategory(this.subCategoryId).subscribe({
      next: (result: any) => {
      this.toast.showDeleteToast("Sub Category Deleted Successfully", 'close');
        this.dialogRef.close({ success: true });
        this.eventEmitter.onDeleteEvent();
      },
      error: (error: any) => {
        this.errorData = this.errorCodeHandle.getErrorMessage(
          error.error.errorCode
        );
        if (this.errorData === '007') {
          this.route.navigateByUrl('/error');
        } else {
          this.toast.showErrorToast(this.errorData, 'close');
        } }
    });
  } 
  ngOnDestroy(): void {
    if (this.dialogRef) { // check if dialogRef is defined before closing
      this.dialogRef.close();
    }

    
  }
}
