import { TestBed } from '@angular/core/testing';

import { SidebarPaddingService } from './sidebar-padding.service';

describe('SidebarPaddingService', () => {
  let service: SidebarPaddingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SidebarPaddingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
