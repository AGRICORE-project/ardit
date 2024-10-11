import { Component, OnInit } from '@angular/core';
import { RoutesNames } from 'src/app/shared/utils/routes.names';
import { AbstractControl, FormControl, FormGroup, Validators, FormBuilder } from '@angular/forms';
import { PasswordMatcher } from '../../../shared/utils/password-matcher.validator';
import { AuthService } from 'src/app/services/auth/auth.service';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['../auth.component.css']
})
export class ResetPasswordComponent implements OnInit {
  
  loginRoute = `/${RoutesNames.LOGIN}`;

  resetPasswordForm: FormGroup;

  spinner: boolean = false;

  success: string;

  token: string;

  error: any;

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {

    document.body.classList.add('login-body');
    
    this.getToken();

    this.resetPasswordForm = this.formBuilder.group({
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8)
      ]),
      password2: new FormControl('', [
        Validators.required,
        Validators.minLength(8)
      ]),
    }, {
      validators: PasswordMatcher('password', 'password2')
    });
    this.error = '';
    this.success = '';
  }

  private getToken() {
    this.activatedRoute.queryParams.subscribe(
      params => params.token ? this.token = params.token : this.error = 'Reset password token not found'
    )
  }

  /**
   * Reset password submit
   */
  resetPassword(): void {
    this.spinner = true;
    this.authService.resetPassword(this.token, this.resetPasswordForm.value.password)
    .pipe(finalize( () => this.spinner = false))
    .subscribe(
      res => this.success = res,
      err => this.error = err.error.message
    )
    this.resetPasswordForm.reset();
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

  ngOnDestroy(): void {
    document.body.classList.remove('login-body');
  }


  handleError(err: any): any{
    if (err.status === 400) {
    } else {
      this.error = err.error.message;
    }
  }

}
