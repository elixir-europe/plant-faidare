import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GermplasmCardComponent } from './germplasm-card.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { GnpisService } from '../gnpis.service';
import { HomeComponent } from '../home/home.component';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute, RouterModule } from '@angular/router';

describe('GermplasmCardComponent', () => {
    let component: GermplasmCardComponent;
    let fixture: ComponentFixture<GermplasmCardComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [GermplasmCardComponent],
            providers: [HttpClientTestingModule]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(GermplasmCardComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });
});
