import { TestBed } from '@angular/core/testing';

import { DataFormatingService } from './data-formating.service';

describe('DataFormatingService', () => {
  let service: DataFormatingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DataFormatingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
