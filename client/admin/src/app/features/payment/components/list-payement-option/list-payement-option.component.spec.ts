import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListPayementOptionComponent } from './list-payement-option.component';

describe('ListPayementOptionComponent', () => {
  let component: ListPayementOptionComponent;
  let fixture: ComponentFixture<ListPayementOptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListPayementOptionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListPayementOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
