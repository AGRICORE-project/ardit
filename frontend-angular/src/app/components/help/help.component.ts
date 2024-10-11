import { Component, OnInit } from '@angular/core';
import { AngularEditorConfig } from '@kolkov/angular-editor';
import { User } from 'src/app/models/user';
import { AlertService } from 'src/app/services/alert/alert.service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { HelpService } from '../../services/help/help.service';

@Component({
  selector: 'app-help',
  templateUrl: './help.component.html',
  styleUrls: ['./help.component.css']
})
export class HelpComponent implements OnInit {

  disabled: boolean = true;

  htmlContent: string;
  helpText: string;

  currentUser: User;

  // Config options for alerts
  options = {
      autoClose: true,
      keepAfterRouteChange: false
  };

  //WYSIWYG OPTIONS
  editorConfig: AngularEditorConfig = {
    editable: true,
      spellcheck: true,
      minHeight: '800',
      width: 'auto',
      translate: 'yes',
      enableToolbar: true,
      showToolbar: true,
      placeholder: 'Enter text here...',
      fonts: [
        {class: 'arial', name: 'Arial'},
        {class: 'times-new-roman', name: 'Times New Roman'},
        {class: 'calibri', name: 'Calibri'},
        {class: 'comic-sans-ms', name: 'Comic Sans MS'}
      ],
    sanitize: true,
    toolbarPosition: 'top',
    toolbarHiddenButtons: [
      [
        'subscript',
        'superscript'
      ],
      [
        'customClasses',
        'insertImage',
        'insertVideo',
        'toggleEditorMode'
      ]
    ]
  };

  constructor(private helpService: HelpService, private authService: AuthService, private alertService: AlertService) { }

  ngOnInit(): void {
    this.getBody();
    this.currentUser = this.authService.getCurrentUserValue();
  }

  getBody() {
    this.helpService.getHelpBody().subscribe(
      res => {
        this.helpText = res.content,
        this.htmlContent = this.helpText
      },
      err => this.handleError(err)
    );
  }

  updateHelp() {
    this.helpService.updateHelpBody(this.htmlContent, this.currentUser.username).subscribe(
      res => {
        this.alertService.success('Help page updated', this.options);
        this.helpText = '';
        this.getBody();
      },
      err => this.handleError(err)
    )

  }

  handleError(err: any): any{
    if(err.status === 403) {
      this.alertService.warn("You do not have permission to perform this action", this.options);
    } else if(err.error.message) {
      this.alertService.error(err.error.message, this.options);
    }
  }

}
