import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VendorOrderDetailViewComponent } from './vendor-order-detail-view.component';

describe('VendorOrderDetailViewComponent', () => {
  let component: VendorOrderDetailViewComponent;
  let fixture: ComponentFixture<VendorOrderDetailViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VendorOrderDetailViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VendorOrderDetailViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
