import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

import { AuthService } from '../../services/auth/auth.service';
import { RoutesNames } from '../utils/routes.names';
import { AlertService } from '../../services/alert/alert.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate{
  
  // Config options for alerts
  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  constructor(private router: Router, private authService: AuthService, private alertService: AlertService){}
  
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const currentUser = this.authService.getCurrentUserValue();
    if (currentUser) {
      // User is logged in
      return true;
    }
    // User not logged in
    this.alertService.warn('You do not have permission to access this section', this.optionsRouteChange);
    this.router.navigate([`/${RoutesNames.LOGIN}`]);
    return false;
  }
}
