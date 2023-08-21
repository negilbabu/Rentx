import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserProfileDetailComponent } from './user-profile-detail.component';

describe('UserProfileDetailComponent', () => {
  let component: UserProfileDetailComponent;
  let fixture: ComponentFixture<UserProfileDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserProfileDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserProfileDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
