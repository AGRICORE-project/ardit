import { Component, OnInit } from '@angular/core';

import { RoutesNames } from '../../../shared/utils/routes.names';

@Component({
  selector: 'app-not-found',
  templateUrl: './not-found.component.html',
  styleUrls: ['./not-found.component.css']
})
export class NotFoundComponent implements OnInit {

  homeRoute = `/${RoutesNames.DATASETS}/`;

  constructor() { }

  ngOnInit(): void {
  }

}
