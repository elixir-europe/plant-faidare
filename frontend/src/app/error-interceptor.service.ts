import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { tap } from 'rxjs/operators';

export interface HttpError {
    status: number | null;
    message: string;
}

@Injectable({
    providedIn: 'root'
})
export class ErrorInterceptorService implements HttpInterceptor {

    private errorSubject = new Subject<HttpError>();

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            tap(null, err => this.errorSubject.next(this.toError(err)))
        );
    }

    private toError(errorResponse: HttpErrorResponse) {
        const errorBody = errorResponse.error;
        if (errorBody instanceof ErrorEvent) {
            // A client-side or network error occurred.
            return {
                status: null,
                message: errorBody.message
            };
        } else {
            // The backend returned an unsuccessful response code.
            let message = errorResponse.message;
            if (errorBody && errorBody.metadata && errorBody.metadata.status) {
                message = errorBody.metadata.status[0].name;
            }
            return {
                status: errorResponse.status,
                message
            };
        }
    }

    getErrors(): Observable<HttpError> {
        return this.errorSubject.asObservable();
    }

}
