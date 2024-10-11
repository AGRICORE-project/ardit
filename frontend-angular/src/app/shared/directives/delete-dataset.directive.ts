import { Directive, Input, OnInit, TemplateRef, ViewContainerRef } from '@angular/core';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth/auth.service';

/***
 * Directive to hide/show the 'delete' button for a dataset based on the role, according to D1.9 table
 */
@Directive({
  selector: '[canDeleteDataset]'
})
export class DeleteDatasetDirective implements OnInit {

  @Input() public canDeleteDataset: string; //author of the dataset
  
  constructor(private viewContainerRef: ViewContainerRef, private templateRef: TemplateRef<any>, private authService: AuthService) { }

  ngOnInit(): void {
    
    const currentUser: User = this.authService.getCurrentUserValue();
    const rolesPermited: string[] = ['ADMIN', 'MANTAINER']

    if (!currentUser) { 
      this.viewContainerRef.clear(); //do not show, is not logged in
      return;
    }

    if (rolesPermited.some( item => currentUser.roles.includes(item))) {
      this.viewContainerRef.createEmbeddedView(this.templateRef); // The user has permission
      return;
    }

    if (this.canDeleteDataset == currentUser.username && currentUser.roles.includes('EDITOR')) {
      this.viewContainerRef.createEmbeddedView(this.templateRef); // The editor is the author, has permission according to table D1.9
      return;
    }
    this.viewContainerRef.clear(); //do not show, doesn't have permission
  }

}
