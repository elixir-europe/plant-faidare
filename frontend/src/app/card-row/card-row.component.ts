import { Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'gpds-card-row',
    templateUrl: './card-row.component.html',
    styleUrls: ['./card-row.component.scss']
})
export class CardRowComponent implements OnInit {

    @Input() label: string;
    @Input() test: any;

    constructor() {
    }

    ngOnInit() {
        /*console.log(this.label);
        console.log(this.test);*/
    }

}
