import { Component, ContentChild, Input, TemplateRef } from '@angular/core';

@Component({
    selector: 'gpds-card-row',
    templateUrl: './card-row.component.html',
    styleUrls: ['./card-row.component.scss']
})
export class CardRowComponent {

    @Input() label: string;
    @Input() test: any = true;

    @ContentChild(TemplateRef) template: TemplateRef<any>;

}
