import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup,  FormControl, FormBuilder, Validators, AbstractControl} from '@angular/forms';

import { AuthService } from '../../../services/auth/auth.service';
import { RoutesNames } from '../../../shared/utils/routes.names';
import { PasswordMatcher } from '../../../shared/utils/password-matcher.validator';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['../auth.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm: FormGroup;

  loginRoute = `/${RoutesNames.LOGIN}`;

  success: any;

  error: any;

  public spinner: boolean = false;

  constructor(private router: Router, private authService: AuthService, private formBuilder: FormBuilder) {}

  ngOnInit(): void {

    document.body.classList.add('login-body');

    // Check if the user is already logged in
    const currentUser = this.authService.getCurrentUserValue();
    currentUser ? this.router.navigate([`/${RoutesNames.DATASETS}`]) : this.router.navigate([`/${RoutesNames.REGISTER}`]);

    this.registerForm = this.formBuilder.group({
      username: new FormControl('', [
        Validators.required
      ]),
      email: new FormControl('', [
        Validators.required,
        Validators.email
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8)
      ]),
      confirmPassword: new FormControl('', [
        Validators.required,
        Validators.minLength(8)
      ])
    }, {
      validator: PasswordMatcher('password', 'confirmPassword')
    });

    this.error = '';
    this.success = '';
  }

  ngOnDestroy() {
    document.body.classList.remove('login-body');
  }

  /**
   * Sign up submit
   */
  signUp(): void{
    this.spinner = true;
    this.error = '';
    this.success = '';
    const user = this.registerForm.value;

    this.authService.signUp(user.username, user.email, user.password)
    .pipe(finalize( () => this.spinner = false))
    .subscribe(
      res => this.success = res,
      err => this.error = err.error.message
    );

    this.registerForm.reset();
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
