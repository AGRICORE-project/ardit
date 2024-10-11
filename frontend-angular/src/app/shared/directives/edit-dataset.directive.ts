import { Directive, Input, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth/auth.service';

/***
 * Directive to hide/show the 'edit' button for a dataset based on the role, according to D1.9 table
 */
@Directive({
  selector: '[canEditDataset]'
})
export class EditDatasetDirective implements OnInit {

  @Input() public canEditDataset: string; //author of the dataset

  constructor(private viewContainerRef: ViewContainerRef, private templateRef: TemplateRef<any>, private authService: AuthService) { }

  ngOnInit(): void {
    
    const currentUser: User = this.authService.getCurrentUserValue();
    const rolesPermited: string[] = ['ADMIN', 'MANTAINER', 'EDITOR']

    if (!currentUser) { 
      this.viewContainerRef.clear(); //do not show, is not logged in
      return;
    }

    if (rolesPermited.some( item => currentUser.roles.includes(item))) {
      this.viewContainerRef.createEmbeddedView(this.templateRef); // The user has permission
      return;
    }

    if (this.canEditDataset == currentUser.username  && currentUser.roles.includes('USER')) {
      this.viewContainerRef.createEmbeddedView(this.templateRef); // The user is the author, has permission according to table D1.9
      return;
    }
    this.viewContainerRef.clear(); //do not show, doesn't have permission
  }


}
