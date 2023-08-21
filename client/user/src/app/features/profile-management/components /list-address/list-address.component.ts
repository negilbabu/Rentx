import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { AddresListService } from '../../services/addres-service';
import { AlertService } from 'src/app/shared/services/alert.service';
import { EventEmitterService } from 'src/app/services/event-emitter.service';
import { ToastService } from 'src/app/shared/services/toast.service';


@Component({
  selector: 'app-list-address',
  templateUrl: './list-address.component.html',
  styleUrls: ['./list-address.component.css']
})
export class ListAddressComponent implements OnInit {

  addressData: any;
  totalAddress: any;
  showDropdown: any;
  items: any[] = [];
  closeFlag: any;
  isConfirm= false;

  constructor(private addressService: AddresListService, private alertService:AlertService,
    private eventEmitter: EventEmitterService,
    public toast: ToastService,) {
    }
   

  ngOnInit(): void {
this.getAddressData();

  }
  getAddressData(){
    this.addressService.getAddressList().subscribe((res: any) => {
     
      this.addressData = res;
      this.totalAddress = res.length;

      console.log(res);

    });
  }
  deleteAddress(addressId:any){
    this.addressService.deleteAddress(addressId).subscribe({
      next: (res) => {
        this.eventEmitter.onDeleteEvent();
        this.isConfirm = false;
        this.toast.showDeleteToast('Address Deleted', 'close'); 
        this.getAddressData();
      },
    });

  }

  requestHandle(address: any) {
    this.isConfirm = true;
    this.alertService
      .openAlert('Do you want to delete the address?')
      .subscribe((res) => {
        if (res) {
          this.deleteAddress(address.id);
        } else {
          this.isConfirm = false;
          return;
        }
      });
  }



  

}