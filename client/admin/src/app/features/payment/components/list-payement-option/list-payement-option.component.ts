import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Subscription, Subject, debounceTime } from 'rxjs';
import { CategoryModalService } from 'src/app/features/category-management/services/category-modal.service';
import { ApiErrorHandlerService } from 'src/app/services/api-error-handler.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';
import { PaymentModalService } from '../../services/payment-modal.service';
import { PaymentService } from '../../services/payment.service';
import { AlertService } from 'src/app/shared/services/alert.service';

@Component({
  selector: 'app-list-payement-option',
  templateUrl: './list-payement-option.component.html',
  styleUrls: ['./list-payement-option.component.css']
})
export class ListPayementOptionComponent implements OnInit {
  searchControl!: FormControl;
  searchForm!: FormGroup;
  getCategorySubscription!: Subscription;
  payments: any[] = [];
  eventSubscription!: Subscription;
  responseLength: any;
  lastPage!: number;
  currentPage: number = 1;
  errorData: any;
  private searchSubject = new Subject<string>();
  Message = 'Are you sure you want to delete?';
  itemCount: any;
  constructor(private alertService:AlertService,
    private dialog: MatDialog,
    private eventEmitter: EventEmitterService,
    private router: Router,
    public ModalService: PaymentModalService,
    private paymentService: PaymentService,
    public toast: ToastService,
    private errorCodeHandle: ApiErrorHandlerService
  ) {
    this.searchControl = new FormControl('', Validators.required);
    this.searchForm = new FormGroup({
      searchInput: this.searchControl,
    });
  }

  ngOnInit(): void {
    this.getPaymentList();
    this.eventSubscription = this.eventEmitter
      .getonSaveEvent()
      .subscribe(() => {
        this.params.sort = 'updatedAt';
        this.params.direction = 'DESC';
        this.getPaymentList();
      });
    this.eventEmitter.getonDeleteEvent().subscribe(() => {
      if (this.itemCount == 1 && this.currentPage > 1) {
        this.previousPage();
      }

      this.getPaymentList();
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
        this.getPaymentList();
      }
      return;
    }

    this.params.search = encodeURIComponent(searchTerm);
    this.params.page = 1;
    this.getPaymentList();
  }
  clearSearch() {
    this.searchForm.reset();
    this.params.search = '';
    this.params.page = 1;
    this.getPaymentList();
  }

  addPayment() {
    this.ModalService.addPayment().subscribe();
  }
  editPayemnt(id: number) {
    this.ModalService.editPayment(id).subscribe();
  }
  gotoSubCategory(categoryid: any): void {
    this.router.navigate(['/subcategories', categoryid]);
  }
  
  page = this.currentPage;
  params = {
    search: '',
    page: this.page,
    size: '7',
    sort: 'updatedAt',
    direction: 'DESC',
  };
  getPaymentList() {
    const queryString = Object.entries(this.params)
      .map(([key, value]) => `${key}=${value}`)
      .join('&');

    this.getCategorySubscription = this.paymentService
      .getPayment()
      .subscribe({
        next: (result: any) => {
          this.payments = result;
          
          this.responseLength = this.payments.length;
          
          
          
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
    this.getPaymentList();
  }
  previousPage() {
    this.params.page--;
    this.getPaymentList();
  }
  toggleSortDirection() {
    // Toggle the sort direction between ASC and DESC
    this.params.sort = 'name';
    this.params.page = 1;
    this.params.direction = this.params.direction === 'ASC' ? 'DESC' : 'ASC';
    this.getPaymentList();
  }
  ngOnDestroy() {
    this.getCategorySubscription.unsubscribe();
    this.eventSubscription.unsubscribe();
    this.dialog.closeAll();
  }
  onDelete(id:any) {
    this.alertService.openAlert(this.Message).subscribe((res) => {
      if (res) {
        this.paymentService.deletePayment(id).subscribe(() => {
          this.getPaymentList();
          this.toast.showDeleteToast('Payment method deleted','close')
        });
      } else {
        return;
      }
    });
  }
  
  }

  
    
