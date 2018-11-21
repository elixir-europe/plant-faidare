import { TestBed } from '@angular/core/testing';

import { GnpisService } from './gnpis.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('GnpisService', () => {
    beforeEach(() => TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [HttpClientTestingModule]
    }));

    it('should be created', () => {
        const service: GnpisService = TestBed.get(GnpisService);
        expect(service).toBeTruthy();
    });
});
