import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { CategoryModalService } from '../../services/category-modal.service';
import { CategoryService } from '../../services/category.service';
import { Subject, Subscription, debounceTime } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css'],
})
export class CategoryListComponent implements OnInit, OnDestroy {
  searchControl!: FormControl;
  searchForm!: FormGroup;
  getCategorySubscription!: Subscription;
  categories: any[] = [];
  eventSubscription!: Subscription;
  responseLength: any;
  lastPage!: number;
  currentPage: number = 1;
  errorData: any;
  private searchSubject = new Subject<string>();
  itemCount: any;
  constructor(
    private dialog: MatDialog,
    private eventEmitter: EventEmitterService,
    private router: Router,
    public ModalService: CategoryModalService,
    private categoryService: CategoryService,
    public toast: ToastService,
    private errorCodeHandle: ApiErrorHandlerService
  ) {
    this.searchControl = new FormControl('', Validators.required);
    this.searchForm = new FormGroup({
      searchInput: this.searchControl,
    });
  }

  ngOnInit(): void {
    this.getCategory();
    this.eventSubscription = this.eventEmitter
      .getonSaveEvent()
      .subscribe(() => {
        this.params.sort = 'updatedAt';
        this.params.direction = 'DESC';
        this.getCategory();
      });
    this.eventEmitter.getonDeleteEvent().subscribe(() => {
      if (this.itemCount == 1 && this.currentPage > 1) {
        this.previousPage();
      }

      this.getCategory();
    });

    this.searchSubject.pipe(debounceTime(500)).subscribe((searchTerm) => {
      // Perform search here
      this.search(searchTerm);
    });
  }

  onKeyUp(event: Event): void {
    const searchTerm = (event.target as HTMLInputElement).value;
    this.searchSubject.next(searchTerm);
  }
  search(searchTerm: string) {
    if (searchTerm.trim().length === 0 || searchTerm[0] === ' ') {
      if (searchTerm.length == 0) {
        this.params.search = encodeURIComponent(searchTerm);
        this.params.page = 1;
        this.getCategory();
      }
      return;
    }

    this.params.search = encodeURIComponent(searchTerm);
    this.params.page = 1;
    this.getCategory();
  }
  clearSearch() {
    this.searchForm.reset();
    this.params.search = '';
    this.params.page = 1;
    this.getCategory();
  }

  addCategory() {
    this.ModalService.addCategory().subscribe();
  }
  deleteCategory(id: number) {
    this.ModalService.deleteCategory(id).subscribe();
  }
  gotoSubCategory(categoryid: any): void {
    this.router.navigate(['/subcategories', categoryid]);
  }
  editCategoryName(id: number) {
    this.ModalService.editCategory(id).subscribe();
  }
  page = this.currentPage;
  params = {
    search: '',
    page: this.page,
    size: '7',
    sort: 'updatedAt',
    direction: 'DESC',
  };
  getCategory() {
    const queryString = Object.entries(this.params)
      .map(([key, value]) => `${key}=${value}`)
      .join('&');

    this.getCategorySubscription = this.categoryService
      .getCategory(queryString)
      .subscribe({
        next: (result: any) => {
          this.categories = result.result;
          this.responseLength = result.numItems;
          this.lastPage = result.lastPage;
          this.currentPage = result.currentPage;
          this.itemCount = result.result.length;
        },
        error: (error: any) => {
          this.errorData = this.errorCodeHandle.getErrorMessage(
            error.error.errorCode
          );
          if (this.errorData === '007') {
            this.router.navigateByUrl('/error');
          } else {
            this.toast.showErrorToast(this.errorData, 'close');
          }
        },
      });
  }
  nextPage() {
    this.params.page++;
    this.getCategory();
  }
  previousPage() {
    this.params.page--;
    this.getCategory();
  }
  toggleSortDirection() {
    // Toggle the sort direction between ASC and DESC
    this.params.sort = 'name';
    this.params.page = 1;
    this.params.direction = this.params.direction === 'ASC' ? 'DESC' : 'ASC';
    this.getCategory();
  }
  ngOnDestroy() {
    this.getCategorySubscription.unsubscribe();
    this.eventSubscription.unsubscribe();
    this.dialog.closeAll();
  }
}
