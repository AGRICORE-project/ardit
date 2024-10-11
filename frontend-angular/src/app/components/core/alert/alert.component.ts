import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { Subscription } from 'rxjs';

import { Alert, AlertType } from '../../../models/alert';
import { AlertService } from '../../../services/alert/alert.service';

@Component({ 
    selector: 'alert', 
    templateUrl: 'alert.component.html', 
    styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit, OnDestroy {
  @Input() id = 'default-alert';
  @Input() fade = true;

  alerts: Alert[] = [];
  alertTitle: string = "";
  alertSubscription: Subscription;
  routeSubscription: Subscription;

  constructor(private router: Router, private alertService: AlertService) { }

  ngOnInit() {
      // Subscription to new alert notifications
      this.alertSubscription = this.alertService.onAlert(this.id).subscribe(alert => {
            // Clear alerts when an empty alert is received
            if (!alert.message) {
                // Filter out alerts without 'keepAfterRouteChange' flag
                this.alerts = this.alerts.filter(x => x.keepAfterRouteChange);

                // Remove 'keepAfterRouteChange' flag on the rest
                this.alerts.forEach(x => delete x.keepAfterRouteChange);
                return;
            }

            this.selectAlertTitle(alert);

            // Add a new alert to the list
            this.alerts.push(alert);

            // Close the alert after the timeout
            if (alert.autoClose) {
                setTimeout(() => this.removeAlert(alert), 5000);
            }
        });

      // Clear alerts on location change
      this.routeSubscription = this.router.events.subscribe(event => {
          if (event instanceof NavigationStart) {
              this.alertService.clear(this.id);
            }
        });
    }
    
    ngOnDestroy() {
        // Unsubscription
        this.alertSubscription.unsubscribe();
        this.routeSubscription.unsubscribe();
    }

    removeAlert(alert: Alert) {
        // Check if the alert has not already been removed
        if (!this.alerts.includes(alert)) return;
        
        if (this.fade) {
            // Fade out alert
            this.alerts.find(x => x === alert).fade = true;

            // Remove alert
            setTimeout(() => {
                this.alerts = this.alerts.filter(x => x !== alert);
            }, 250);
        } else {
            // remove alert
            this.alerts = this.alerts.filter(x => x !== alert);
        }
    }
    
    selectTypeOfAlert(alert: Alert) {
        if (!alert) return;
        
        // Select the correct CSS classes according to alert type
        const classes = ['alert', 'alert-dismissable'];
              
        const alertTypeClass = {
            [AlertType.Success]: 'alert-success',
            [AlertType.Error]: 'alert-danger',
            [AlertType.Info]: 'alert-info',
            [AlertType.Warning]: 'alert-warning'
        }

        classes.push(alertTypeClass[alert.type]);

        if (alert.fade) {
            classes.push('fade');
        }

        return classes.join(' ');
    }

    selectAlertTitle(alert: Alert) {
        if (!alert) return;
              
        const alertTypeClass = {
            [AlertType.Success]: 'Success',
            [AlertType.Error]: 'Error',
            [AlertType.Info]: 'Info',
            [AlertType.Warning]: 'Warning'
        }

        this.alertTitle = alertTypeClass[alert.type];
    }
}