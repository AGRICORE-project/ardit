import { Directive, Input, TemplateRef, ViewContainerRef } from '@angular/core';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth/auth.service';

@Directive({
  selector: '[canUpdateComment]'
})
export class UpdateCommentDirective {

  @Input() public canUpdateComment: string; //Comment author

  constructor(private viewContainerRef: ViewContainerRef, private templateRef: TemplateRef<any>, private authService: AuthService) { }

  public ngOnInit() { 

    const currentUser: User = this.authService.getCurrentUserValue();

    if (!currentUser) {
      this.viewContainerRef.clear();
      return;
    }

    if (currentUser.username == this.canUpdateComment) {
      this.viewContainerRef.createEmbeddedView(this.templateRef); // The user is the author or has permission, show the button
    } else { 
      this.viewContainerRef.clear(); // The user is not the author or don't have permissions, hide the button
    }
  }

}
