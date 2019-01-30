import { Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'gpds-card-section',
    templateUrl: './card-section.component.html',
    styleUrls: ['./card-section.component.scss']
})
export class CardSectionComponent implements OnInit {

    @Input() header: string;
    @Input() test: any;
    @Input() table: any;

    constructor() {
    }

    ngOnInit() {
        console.log(this.table);
    }

}
