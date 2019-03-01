import { Component, Input, OnInit } from '@angular/core';
import { ErrorInterceptorService } from '../error-interceptor.service';

@Component({
    selector: 'gpds-loading-spinner',
    template: `
        <div *ngIf="loading" class="loading">
          <i class="fa fa-spin fa-spinner" title="Loading..."></i>
        </div>
    `,
    styleUrls: ['./loading-spinner.component.scss']
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
