import { Directive, OnInit, Input, ViewContainerRef, TemplateRef } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { User } from '../../models/user';

/***
 * Directive to hide/show the 'permanent erase' button for each comment based on the user roles
 */
@Directive({
  selector: '[canPurgeComment]'
})
export class PurgeCommentDirective implements OnInit {

  @Input() public canPurgeComment: Array<string>; // List of roles allowed

  constructor(private viewContainerRef: ViewContainerRef, private templateRef: TemplateRef<any>, private authService: AuthService) { }

  public ngOnInit() { 

    let currentUser: User = this.authService.getCurrentUserValue();

    if (!currentUser) {
      this.viewContainerRef.clear();
      return;
    }

    if (currentUser.roles.findIndex((element) => this.canPurgeComment.indexOf(element) !== -1) < 0) {
      this.viewContainerRef.clear(); // The user does not have the allowed roles, hide the content
    } else { 
      this.viewContainerRef.createEmbeddedView(this.templateRef); // Ther user has the allowed roles, show the content
    }
  }

}
