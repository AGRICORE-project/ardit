import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

import { AuthService } from '../../services/auth/auth.service';
import { AlertService } from '../../services/alert/alert.service';
import { RoutesNames } from '../utils/routes.names';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate{

  // Config options for alerts
  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  constructor(private router: Router, private authService: AuthService, private alertService: AlertService){}
  
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      
    const currentUser = this.authService.getCurrentUserValue();
    
    if (currentUser) {
      const userRoles: string[] = currentUser.roles;

      // Check if the user roles are in the list of allowed roles
      // Filter the role array, any role not allowed is not added to the filtered list
      // If the filtered list length is 0, means that the user has no valid role
      if(userRoles.filter((role) => route.data.roles.indexOf(role) !== -1).length > 0) {
        // User has permission
        return true;
      }

      // Role not authorized, redirect to home page
      this.alertService.warn('You do not have permission to access this section', this.optionsRouteChange);
      this.router.navigate([`/${RoutesNames.DATASETS}`]); //TODO: go back to previous page
      return false;
    }
    
    // User not logged in
    this.alertService.warn('You do not have permission to access this section', this.optionsRouteChange);
    this.router.navigate([`/${RoutesNames.LOGIN}`]);
    return false;
  }
}