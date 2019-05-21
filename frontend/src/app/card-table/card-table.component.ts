import { Component, ContentChild, Input, TemplateRef } from '@angular/core';

@Component({
    selector: 'faidare-card-table',
    templateUrl: './card-table.component.html',
    styleUrls: ['./card-table.component.scss']
})
export class CardTableComponent {

    @Input() headers: string[];
    @Input() rows: any[];

    @ContentChild(TemplateRef) template: TemplateRef<any>;

}
