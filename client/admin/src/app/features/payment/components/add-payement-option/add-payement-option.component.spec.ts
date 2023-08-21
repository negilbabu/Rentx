import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPayementOptionComponent } from './add-payement-option.component';

describe('AddPayementOptionComponent', () => {
  let component: AddPayementOptionComponent;
  let fixture: ComponentFixture<AddPayementOptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddPayementOptionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddPayementOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
