import { Component, OnInit } from '@angular/core';

import { RoutesNames } from '../../../shared/utils/routes.names';
import { AuthService } from '../../../services/auth/auth.service';
import { User } from '../../../models/user';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SubscriptionService } from '../../../services/subscription/subscription.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  loginRoute = `/${RoutesNames.LOGIN}`;
  datasetsRoute = `/${RoutesNames.DATASETS}`;
  datasetCreationRoute = `/${RoutesNames.DATASET_CREATE}`;
  datasetDescriptionsRoute = `/${RoutesNames.DATASETS_DESCRIPTIONS}`;
  cataloguesRoute = `/${RoutesNames.CATALOGUES}`;
  vocabulariesRoute = `/${RoutesNames.VOCABULARIES}`;
  helpRoute = `/${RoutesNames.HELP}`;
  contactRoute = `/${RoutesNames.CONTACT}`;
  usersRoute = `/${RoutesNames.USERS}`;
  profile = `/${RoutesNames.PROFILE}`;
  justUser: boolean;

  //Subscription
  subsText: string = ''
  subsError: string = ''
  

  public currentUser: User;
  public isMenuCollapsed = true;

  constructor(private authService: AuthService, private modalService: NgbModal, private subscriptionService: SubscriptionService) { }

  ngOnInit(): void {
    this.getCurrentUserData();
    this.isJustUser();
  }

  public getCurrentUserData(): void {
    this.currentUser = this.authService.getCurrentUserValue();
  }

  public logout(): void {
    this.authService.logout().subscribe();
  }

  private isJustUser() {
    if (this.currentUser) {
    (this.currentUser.roles.includes('USER') && this.currentUser.roles.length === 1) ? this.justUser = true : this.justUser = false;
   }
  }

  public openSubscribeModal(modal: any) {
    this.modalService.open(modal, { size: 'md', backdrop: 'static' }).result.then((result) => {}, (reason) => {});
  }

  public datasetSubscribe(){
    this.subscriptionService.dtsubscribe().subscribe(
      res => {
        this.currentUser.subscribed = true
        this.subsText = 'You have successfully subscribed.'
      },
      err => {
        this.subsError = err.error.message
      }
    )
  }

  public datasetUnsubscribe(){
    this.subscriptionService.dtunsubscribe().subscribe(
      res => {
        this.currentUser.subscribed = false
        this.subsText = 'You are not subscribed now.'
      },
      err => {
        this.subsError = err.error.message
      }
    )
  }

}

