import { TestBed } from '@angular/core/testing';

import { BrapiService } from './brapi.service';

describe('BrapiService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BrapiService = TestBed.get(BrapiService);
    expect(service).toBeTruthy();
  });
});
