import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VendorOrderListComponent } from './vendor-order-list.component';

describe('VendorOrderListComponent', () => {
  let component: VendorOrderListComponent;
  let fixture: ComponentFixture<VendorOrderListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VendorOrderListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VendorOrderListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
