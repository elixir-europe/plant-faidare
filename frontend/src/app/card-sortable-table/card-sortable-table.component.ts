import { Component, ContentChild, Input, OnInit, QueryList, TemplateRef, ViewChildren } from '@angular/core';
import { Observable } from 'rxjs';
import { Country } from './country';
import { CountryService } from './country.services';
import { NgbdSortableHeader, SortEvent } from './sortable.directive';

@Component({
  selector: 'faidare-card-sortable-table',
  templateUrl: './card-sortable-table.component.html',
  styleUrls: ['./card-sortable-table.component.scss']
})
export class CardSortableTableComponent {

    countries$: Observable<Country[]>;
    total$: Observable<number>;

    @Input() tableHeaders: String[];
    @Input() rows: any;

    @ContentChild(TemplateRef) template: TemplateRef<any>;

    constructor(public service: CountryService) {
        this.countries$ = service.countries$;
        this.total$ = service.total$;
    }


    @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader>;


    onSort({column, direction}: SortEvent) {
        // resetting other headers
        this.headers.forEach(header => {
            console.log(header);
            if (header.sortable !== column) {
                header.direction = '';
            }
        });

        this.service.sortColumn = column;
        this.service.sortDirection = direction;
    }

}
