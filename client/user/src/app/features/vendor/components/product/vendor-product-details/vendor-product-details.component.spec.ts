import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VendorProductDetailsComponent } from './vendor-product-details.component';

describe('VendorProductDetailsComponent', () => {
  let component: VendorProductDetailsComponent;
  let fixture: ComponentFixture<VendorProductDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VendorProductDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VendorProductDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
