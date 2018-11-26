import { async, TestBed } from '@angular/core/testing';

import { HomeComponent } from './home.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { GnpisService } from '../gnpis.service';

describe('HomeComponent', () => {

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            declarations: [HomeComponent],
            providers: [HttpClientTestingModule]
        });
    }));


    it('should create', () => {
        const service: GnpisService = TestBed.get(GnpisService) as GnpisService;
        const component = new HomeComponent(service);
        expect(component).toBeTruthy();
    });
});
