import { Component } from '@angular/core';
import { merge, Observable } from 'rxjs';
import { ErrorInterceptorService, HttpError } from '../error-interceptor.service';
import { NavigationEnd, Router } from '@angular/router';
import { filter, map } from 'rxjs/operators';
import { Location } from '@angular/common';

@Component({
    selector: 'faidare-error',
    templateUrl: './error.component.html',
    styleUrls: ['./error.component.scss']
})
export class ErrorComponent {

    error$: Observable<HttpError | null>;

    constructor(
        private router: Router,
        private errorInterceptor: ErrorInterceptorService,
        private location: Location
    ) {
        this.error$ = merge(
            this.errorInterceptor.getErrors(),
            this.router.events.pipe(
                filter(event => event instanceof NavigationEnd),
                map(() => null)
            )
        );
    }

    canGoBack(): boolean {
        return window.history.length > 1;
    }

    goBack(): void {
        this.location.back();
    }
}
