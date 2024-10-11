import { Component, OnInit, ɵpatchComponentDefWithScope } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs/operators';

import { AuthService } from '../../../services/auth/auth.service';
import { RoutesNames } from '../../../shared/utils/routes.names';

@Component({
  selector: 'app-recover-password',
  templateUrl: './recover-password.component.html',
  styleUrls: ['../auth.component.css']
})
export class RecoverPasswordComponent implements OnInit {

  recoverPasswordForm: FormGroup;

  loginRoute = `/${RoutesNames.LOGIN}`;

  success: string;

  spinner: boolean = false;

  error: any;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {

    document.body.classList.add('login-body');

    // Check if the user is already logged in
    const currentUser = this.authService.getCurrentUserValue();
    currentUser ? this.router.navigate([`/${RoutesNames.DATASETS}`]) : this.router.navigate([`/${RoutesNames.RECOVER_PASSWORD}`]);

    this.recoverPasswordForm = new FormGroup({
      email: new FormControl('', [
        Validators.required,
        Validators.email
      ])
    });

    this.error = '';

  }

  ngOnDestroy() {
    document.body.classList.remove('login-body');
  }

  /**
   * Form submit
   */
   recoverPassword(): void {
    this.spinner = true;
    this.error = ''
    this.success = ''
    const email: string = this.recoverPasswordForm.value.email;

    this.authService.recoverPassword(email)
    .pipe(finalize( () => this.spinner = false))
    .subscribe(
      res => this.success = res,
      err => this.error = err.error.message
    );
  }

  /**
   * Form validation helpers
   */
  public fieldInvalid(form: FormGroup, fieldName: string): boolean {
    const control: AbstractControl = form.controls[fieldName];
    const invalid: boolean = control.invalid;
    const dirty: boolean = control.dirty;
    const touched: boolean = control.touched;
    return invalid && (dirty || touched);
  }

}
