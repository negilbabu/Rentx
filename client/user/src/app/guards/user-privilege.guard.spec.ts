import { TestBed } from '@angular/core/testing';

import { UserPrivilegeGuard } from './user-privilege.guard';

describe('UserPrivilegeGuard', () => {
  let guard: UserPrivilegeGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(UserPrivilegeGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
