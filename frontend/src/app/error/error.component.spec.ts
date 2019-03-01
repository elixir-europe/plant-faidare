import { TestBed } from '@angular/core/testing';
import { NavigationEnd, NavigationStart, Router, RouterEvent } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Subject } from 'rxjs';
import { ComponentTester, speculoosMatchers } from 'ngx-speculoos';

import { Location } from '@angular/common';
import { ErrorComponent } from './error.component';
import { ErrorInterceptorService, HttpError } from '../error-interceptor.service';

class ErrorComponentTester extends ComponentTester<ErrorComponent> {
    constructor() {
        super(ErrorComponent);
    }

    get error() {
        return this.element('.error');
    }

    get status() {
        return this.element('#error-status');
    }

    get message() {
        return this.element('#error-message');
    }
}

describe('ErrorComponent', () => {
    let tester: ErrorComponentTester;

    let routerEvents: Subject<RouterEvent>;
    let httpErrors: Subject<HttpError>;

    beforeEach(() => {
        routerEvents = new Subject<RouterEvent>();
        httpErrors = new Subject<HttpError>();

        TestBed.configureTestingModule({
            declarations: [ ErrorComponent ],
            providers: [
                { provide: Router, useValue: { events: routerEvents } },
                { provide: Location, useValue: { back: () => {} } }
            ],
            imports: [
                HttpClientTestingModule
            ]
        });

        const errorInterceptorService: ErrorInterceptorService = TestBed.get(ErrorInterceptorService);
        spyOn(errorInterceptorService, 'getErrors').and.returnValue(httpErrors);

        tester = new ErrorComponentTester();
        tester.detectChanges();

        jasmine.addMatchers(speculoosMatchers);
    });

    it('should not display any error initially', () => {
        expect(tester.error).toBeNull();
    });

    it('should display an error when it is emitted, and hide it when navigation succeeds', () => {
        httpErrors.next({
            status: 500,
            message: 'Oulala'
        });
        tester.detectChanges();

        expect(tester.error).toContainText('An unexpected error occurred');
        expect(tester.status).toContainText('Status: 500');
        expect(tester.message).toContainText('Message: Oulala');

        httpErrors.next({
            status: null,
            message: null
        });
        tester.detectChanges();

        expect(tester.error).toContainText('An unexpected error occurred');
        expect(tester.status).toBeNull();
        expect(tester.message).toBeNull();

        routerEvents.next(new NavigationStart(1, 'foo', null));
        tester.detectChanges();
        expect(tester.error).not.toBeNull();

        routerEvents.next(new NavigationEnd(1, 'foo', null));
        tester.detectChanges();
        expect(tester.error).toBeNull();
    });
});
