import { TestBed } from '@angular/core/testing';

import { GnpisService } from './gnpis.service';

describe('GnpisService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GnpisService = TestBed.get(GnpisService);
    expect(service).toBeTruthy();
  });
});
