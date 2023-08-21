import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderHistoryDetailComponent } from './order-history-detail.component';

describe('OrderHistoryDetailComponent', () => {
  let component: OrderHistoryDetailComponent;
  let fixture: ComponentFixture<OrderHistoryDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderHistoryDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderHistoryDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
