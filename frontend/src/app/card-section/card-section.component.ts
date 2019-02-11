import { Component, ContentChild, Input, TemplateRef } from '@angular/core';

@Component({
    selector: 'gpds-card-section',
    templateUrl: './card-section.component.html',
    styleUrls: ['./card-section.component.scss']
})
export class CardSectionComponent {

    @Input() header: string;
    @Input() test: any = true;

    @ContentChild(TemplateRef) template: TemplateRef<any>;

}
