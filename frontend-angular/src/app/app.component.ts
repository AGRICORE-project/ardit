import { Component } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';

import { AuthService } from './services/auth/auth.service';
import { RoutesNames } from './shared/utils/routes.names';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Agricore indexer';

  userLoggedIn = false;

  constructor(private authService: AuthService, private router: Router) {
    router.events.forEach((event) => {
      if (event instanceof NavigationStart) { // Check if the user is logged in to show or not header and footer components
        (event['url'] === `/${RoutesNames.LOGIN}`) 
        || (event['url'] === `/${RoutesNames.REGISTER}`) 
        || (event['url'] === `/${RoutesNames.RECOVER_PASSWORD}`) 
        || (event['url'].includes(`/${RoutesNames.RESET_PASSWORD}`) 
        || (event['url'].includes(`/${RoutesNames.CONFIRM_ACCOUNT}`))) ?
          this.userLoggedIn = false : this.userLoggedIn = true;
      }
    });
  }
}
