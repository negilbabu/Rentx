import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StoreDetailedViewComponent } from './store-detailed-view.component';

describe('StoreDetailedViewComponent', () => {
  let component: StoreDetailedViewComponent;
  let fixture: ComponentFixture<StoreDetailedViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StoreDetailedViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StoreDetailedViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
