import { Component, ContentChild, Input, TemplateRef } from '@angular/core';

@Component({
    selector: 'gpds-card-row',
    templateUrl: './card-row.component.html',
    styleUrls: ['./card-row.component.scss']
})
export class CardRowComponent {

    @Input() label: string;
    @Input() test: any = true;
    @Input() value: string;

    @ContentChild(TemplateRef) template: TemplateRef<any>;

    shouldShow(): boolean {
        return this.test && (
            // Value not provided and template provided
            (this.value === undefined && this.template !== undefined)
            ||
            // Or value truthy
            !!this.value
        );
    }
}
