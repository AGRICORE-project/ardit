import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth/auth.service';
import { RoutesNames } from '../../shared/utils/routes.names';
import { User } from '../../models/user';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  /*styleUrls: ['']*/
})
export class HomeComponent implements OnInit {

  public currentUser: User;

  error: any;

  constructor(private router: Router, private authService: AuthService) { }

  ngOnInit(): void {
    this.error = '';
    this.getCurrentUserData();
  }

  public getCurrentUserData(): void{
    this.currentUser = null;
    this.authService.getCurrentUser().subscribe(
      res => this.currentUser = res,
      err => this.handleError(err)
    );
  }

  public logout(): void {
    this.authService.logout().subscribe();
  }

  public getUsers(): void {
    this.router.navigate([`/${RoutesNames.USERS}`]);
  }

  public getDatasets(): void {
    this.router.navigate([`/${RoutesNames.DATASETS}`]);
  }

  handleError(err: any): any{
    this.error = err.error.message;
  }
}
