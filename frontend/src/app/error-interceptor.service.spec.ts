import { TestBed } from '@angular/core/testing';

import { ErrorInterceptorService, HttpError } from './error-interceptor.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';

describe('ErrorInterceptorService', () => {
    let service: ErrorInterceptorService;
    let http: HttpTestingController;
    let httpClient: HttpClient;
    const noop = () => {
    };

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [
                {
                    provide: HTTP_INTERCEPTORS,
                    useExisting: ErrorInterceptorService,
                    multi: true
                }
            ]
        });

        service = TestBed.get(ErrorInterceptorService);
        http = TestBed.get(HttpTestingController);
        httpClient = TestBed.get(HttpClient);
    });

    it('should emit error when error is not an HTTP response', () => {
        let error: HttpError;
        service.getErrors().subscribe(err => {
            error = err;
        });

        httpClient.get('/test').subscribe(null, noop);
        http.expectOne('/test').error(new ErrorEvent('unknown', { message: 'not good' }));

        expect(error.status).toBeNull();
        expect(error.message).toBe('not good');
    });

    it('should emit error when error is an HTTP response', () => {
        let error: HttpError;
        service.getErrors().subscribe(err => {
            error = err;
        });

        httpClient.get('/test').subscribe(null, noop);
        http.expectOne('/test').flush(null, { status: 500, statusText: 'Server Error' });

        expect(error.status).toBe(500);
        expect(error.message).toBe('Http failure response for /test: 500 Server Error');
    });

    it('should emit error when error is a BrAPI error response', () => {
        let error: HttpError;
        service.getErrors().subscribe(err => {
            error = err;
        });

        const result = {
            metadata: {
                status: [{
                    name: 'BrAPI server error description'
                }]
            }
        };

        httpClient.get('/test').subscribe(null, noop);
        http.expectOne('/test').flush(result, { status: 500, statusText: 'Server Error' });

        expect(error.status).toBe(500);
        expect(error.message).toBe(result.metadata.status[0].name);
    });

    it('should emit error when error is a BrAPI error response with multiple errors', () => {
        let error: HttpError;
        service.getErrors().subscribe(err => {
            error = err;
        });

        const result = {
            metadata: {
                status: [{
                    name: 'error 1'
                }, {
                    name: 'error 2'
                }]
            }
        };

        httpClient.get('/test').subscribe(null, noop);
        http.expectOne('/test').flush(result, { status: 500, statusText: 'Server Error' });

        expect(error.status).toBe(500);
        expect(error.message).toBe('error 1; error 2');
    });
});
