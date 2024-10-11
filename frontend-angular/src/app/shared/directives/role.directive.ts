import { Directive, OnInit, Input, ViewContainerRef, TemplateRef } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { User } from '../../models/user';

/***
 * Directive to hide/show content in the UI based on the user roles
 */
@Directive({
  selector: '[checkRoles]'
})
export class RoleDirective implements OnInit {

  @Input() public checkRoles: Array<string>; // List of roles allowed

  /**
   * @param viewContainerRef the location to render the templateRef
   * @param templateRef the templateRef to be rendered
   * @param authService to check the logged user roles
   */
  constructor(private viewContainerRef: ViewContainerRef, private templateRef: TemplateRef<any>, private authService: AuthService) {}

  public ngOnInit() { 

    let currentUser: User = this.authService.getCurrentUserValue();

    if (!currentUser) return

    if (!currentUser.roles) {
      this.viewContainerRef.clear();
    }

    if (currentUser.roles.findIndex((element) => this.checkRoles.indexOf(element) !== -1) < 0) {
      this.viewContainerRef.clear(); // The user does not have the allowed roles, hide the content
    } else { 
      this.viewContainerRef.createEmbeddedView(this.templateRef); // Ther user has the allowed roles, show the content
    }
  }
}
