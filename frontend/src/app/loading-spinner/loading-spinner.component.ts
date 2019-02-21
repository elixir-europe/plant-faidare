import { Component, Input } from '@angular/core';

@Component({
    selector: 'gpds-loading-spinner',
    template: `
        <i *ngIf="loading" class="fa fa-spin fa-spinner" title="Loading..."></i>
    `,
    styles: [`
        i {
            font-size: 2.2em;
        }
    `]
})
export class LoadingSpinnerComponent {

    @Input() loading: boolean;

}
