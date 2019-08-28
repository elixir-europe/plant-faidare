import { Component, OnInit } from '@angular/core';
import { CountryService } from '../card-sortable-table/country.services';
import { Country } from '../card-sortable-table/country';

@Component({
    selector: 'faidare-germplasm-result-page',
    templateUrl: './germplasm-result-page.component.html',
    styleUrls: ['./germplasm-result-page.component.scss']
})
export class GermplasmResultPageComponent implements OnInit {


    countries: Country[];

    constructor(public service: CountryService) { }

    ngOnInit() {

        this.service.countries$.subscribe(countries => {
            this.countries = countries;
        });
    }
}
