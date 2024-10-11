import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup,  FormControl,  Validators , AbstractControl} from '@angular/forms';

import { AuthService } from '../../../services/auth/auth.service';
import { RoutesNames } from '../../../shared/utils/routes.names';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['../auth.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;

  registerRoute = `/${RoutesNames.REGISTER}`;
  recoverPasswordRoute = `/${RoutesNames.RECOVER_PASSWORD}`;
  datasetsRoute = `/${RoutesNames.DATASETS}`;

  currentYear: number;

  error: any;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {

    document.body.classList.add('login-body');

    // Check if the user is already logged in
    const currentUser = this.authService.getCurrentUserValue();
    currentUser ? this.router.navigate([`/${RoutesNames.DATASETS}`]) : this.router.navigate([`/${RoutesNames.LOGIN}`]);

    this.loginForm = new FormGroup({
      username: new FormControl('', [
        Validators.required
      ]),
      password: new FormControl('', [
        Validators.required
      ])
    });

    this.currentYear = new Date().getFullYear();

    this.error = '';
  }

  ngOnDestroy() {
    document.body.classList.remove('login-body');
  }

  /**
   * Sign in submit
   */
  signIn(): void{
    const user = this.loginForm.value;

    this.authService.signIn(user.username, user.password).subscribe(
      _ => this.authService.getCurrentUser().subscribe(
        _ => this.router.navigate([`/${RoutesNames.DATASETS}`]), err => this.handleError(err)),
      err => this.handleError(err)
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

  handleError(err: any): any{
    this.error = err.error.message;
  }
}
