import { Directive, OnInit, Input, ViewContainerRef, TemplateRef } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { User } from '../../models/user';

/***
 * Directive to hide/show the 'delete' button for each comment if the user logged is not the author
 */
@Directive({
  selector: '[canDeleteComment]'
})
export class DeleteCommentDirective implements OnInit {

  @Input() public canDeleteComment: string; //Comment author

  constructor(private viewContainerRef: ViewContainerRef, private templateRef: TemplateRef<any>, private authService: AuthService) { }

  public ngOnInit() { 

    const currentUser: User = this.authService.getCurrentUserValue();
    const rolesPermited: string[] = ['ADMIN', 'MANTAINER']

    if (!currentUser) {
      this.viewContainerRef.clear();
      return;
    }

    if (currentUser.username == this.canDeleteComment || rolesPermited.some( item => currentUser.roles.includes(item))) {
      this.viewContainerRef.createEmbeddedView(this.templateRef); // The user is the author or has permission, show the button
    } else { 
      this.viewContainerRef.clear(); // The user is not the author or don't have permissions, hide the button
    }
  }

}
