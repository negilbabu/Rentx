import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VendorBankDetailsComponent } from './vendor-bank-details.component';

describe('VendorBankDetailsComponent', () => {
  let component: VendorBankDetailsComponent;
  let fixture: ComponentFixture<VendorBankDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VendorBankDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VendorBankDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
