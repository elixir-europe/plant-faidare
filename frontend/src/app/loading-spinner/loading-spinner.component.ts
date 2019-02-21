import { Component, Input, OnInit } from '@angular/core';
import { ErrorInterceptorService } from '../error-interceptor.service';

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
export class LoadingSpinnerComponent implements OnInit {

    @Input() loading: boolean;

    constructor(private errorService: ErrorInterceptorService) {
    }

    ngOnInit(): void {
        // Force loading stop when an error is intercepted
        this.errorService.getErrors().subscribe(() => {
            this.loading = false;
        });
    }
}
