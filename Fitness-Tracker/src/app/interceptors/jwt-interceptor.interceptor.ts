import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { IdentityService } from '../services/identity.service';
import { environment } from 'src/environments/environment';

@Injectable()
export class JwtInterceptorInterceptor implements HttpInterceptor {

  constructor(private identityService: IdentityService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const isApiUrl = request.url.startsWith(environment.apiUrl);
    if (this.identityService.isLogged() && isApiUrl) {
        request = request.clone({
            setHeaders: {
                Authorization: `Bearer ${this.identityService.getJwt()}`
            }
        });
    }

    return next.handle(request);
  }
}
