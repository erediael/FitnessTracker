import { Injectable } from '@angular/core';
import { catchError } from 'rxjs/operators';

import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { IdentityService } from '../services/identity.service';

@Injectable()
export class ErrorInterceptorInterceptor implements HttpInterceptor {

  constructor(private identityService: IdentityService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(catchError(err => {
        if (err.status === 401 || err.status === 403) {
            this.identityService.logout();
        }
        
        const error = err.error.message || err.statusText;
        return throwError(error);
    }));
  }
}
