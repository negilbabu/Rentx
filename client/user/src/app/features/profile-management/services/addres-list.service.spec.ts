import { TestBed } from '@angular/core/testing';

import { AddresListService } from './addres-service';

describe('AddresListService', () => {
  let service: AddresListService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddresListService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
