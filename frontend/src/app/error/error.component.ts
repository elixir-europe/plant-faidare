import { Component } from '@angular/core';
import { merge, Observable } from 'rxjs';
import { ErrorInterceptorService, HttpError } from '../error-interceptor.service';
import { NavigationEnd, Router } from '@angular/router';
import { filter, map } from 'rxjs/operators';


@Component({
    selector: 'gpds-error',
    templateUrl: './error.component.html',
    styleUrls: ['./error.component.scss']
})
export class ErrorComponent {

    error$: Observable<HttpError | null>;

    constructor(private router: Router,
                private errorInterceptor: ErrorInterceptorService) {
        this.error$ = merge(
            this.errorInterceptor.getErrors(),
            this.router.events.pipe(
                filter(event => event instanceof NavigationEnd),
                map(() => null)
            )
        );
    }

}
