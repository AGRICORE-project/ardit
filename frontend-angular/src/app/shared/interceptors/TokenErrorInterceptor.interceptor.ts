import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth/auth.service';

@Injectable()
export class TokenErrorInterceptor implements HttpInterceptor {
    
    constructor(private authenticationService: AuthService, private router: Router) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError(err => {

            if (err instanceof HttpErrorResponse) {
                const authError: boolean = err.status === 401; // Malformed, unsupported, invalid, expired or missing token
                const authError404: boolean = err.status === 404; // Page not found

                // Invalid token, do logout and clean user's browser data
                if (authError) {
                    this.authenticationService.logout();
                }

                // Page not found
                if (authError404) {
                    this.router.navigate([`404`]);
                }
            }

            return throwError(err);
        }));
    }
}
