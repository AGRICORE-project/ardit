import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';

import { AuthService } from '../../../services/auth/auth.service';
import { RoutesNames } from '../../../shared/utils/routes.names';

@Component({
  selector: 'app-confirm-account',
  templateUrl: './confirm-account.component.html',
  styleUrls: ['../auth.component.css']
})
export class ConfirmAccountComponent implements OnInit {
  
  loginRoute = `/${RoutesNames.LOGIN}`;

  success: string = null;
  error: string = null;

  constructor(private router: Router, private route: ActivatedRoute, private authService: AuthService) { }

  ngOnInit(): void {

    document.body.classList.add('login-body');

    // Check if the user is already logged in
    const currentUser = this.authService.getCurrentUserValue();
    if (currentUser) this.router.navigate([`/${RoutesNames.DATASETS}`]);

    // Check token in query params
    this.route.queryParams.subscribe(params => {

      // Check if there are query params
      if(Object.keys(params).length) {
  
        if(params.token) {
          this.confirmAccount(params.token);
        } else {
          // No token param, error
          this.error = "Confirmation token not found"
        }
  
      } else {
  
        // No query params, error
        this.error = "Confirmation token not found"
      }
    });
  }

  ngOnDestroy() {
    document.body.classList.remove('login-body');
  }

  confirmAccount(token: any) {

    this.authService.confirmAccount(token).subscribe(
      res => this.success = res,
      err => this.handleError(err)
    );
  }

  handleError(err: any): any{
    this.error = err.error.message;
  }

}
