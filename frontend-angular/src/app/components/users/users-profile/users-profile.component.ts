import { Component, OnInit } from '@angular/core';
import { FormGroup,  FormControl, AbstractControl, Validators, FormBuilder } from '@angular/forms';

import { AlertService } from '../../../services/alert/alert.service';
import { AuthService } from '../../../services/auth/auth.service';
import { User } from '../../../models/user';
import { CurrentUserCredentials } from '../../../models/current-user-credentials';
import { PasswordMatcher } from '../../../shared/utils/password-matcher.validator';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-users-profile',
  templateUrl: './users-profile.component.html',
  styleUrls: ['./users-profile.component.css']
})
export class UsersProfileComponent implements OnInit {

  // Form groups
  profileForm: FormGroup;
  passwordForm: FormGroup;
  deleteAccountForm: FormGroup;

  profileData: User; // Current user profile data

  // Config options for alerts
  options = {
    autoClose: true,
    keepAfterRouteChange: false
  };

  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  // Navbar section active
  navItemActive = 1;

  constructor(private authService: AuthService, private alertService: AlertService, private formBuilder: FormBuilder, private modalService: NgbModal) { }

  ngOnInit(): void {

    this.getProfileData(); // Get user profile account data

    this.profileForm = this.formBuilder.group({
      username: new FormControl(null, [
        Validators.required,
      ]),
      email: new FormControl(null, [
        Validators.required,
        Validators.email
      ])
    });

    this.passwordForm = this.formBuilder.group({
      currentPassword: new FormControl(null, [
        Validators.required
      ]),
      newPassword: new FormControl(null, [
        Validators.required,
        Validators.minLength(8)
      ]),
      newPasswordConfirm: new FormControl(null, [
        Validators.required,
        Validators.minLength(8)
      ])
    }, {
      validator: PasswordMatcher('newPassword', 'newPasswordConfirm')
    });
  }

  /**
   * Get user profile data
   */
   public getProfileData(): void {

    this.authService.getUser().subscribe(
      res => {
        this.profileData = res,
        this.profileForm.patchValue({ // Populate the form with user data
          username: this.profileData.username,
          email: this.profileData.email
        });
      },
      err => this.handleError(err)
    );
  }

  /**
   * Update user's account data
   */
  public updateAccount(): void {

    const user = this.profileForm.value; // Get form data

    this.authService.updateCurrentUser(user).subscribe(
      res => {
        this.profileData = res,
        this.alertService.success('Account updated', this.options)// Notify success
      },
      err => this.handleError(err)
    );
  }

  /**
   * Update user's password
   */
  public updatePassword(): void {

    const credentials: CurrentUserCredentials = this.passwordForm.value; // Get form data
    credentials.username = this.profileData.username;

    this.authService.updateCurrentUserPassword(credentials).subscribe(
    _ => {
      this.passwordForm.reset(); // Reset the form
      this.alertService.success('Password updated', this.options); // Notify success
    },
      err => this.handleError(err)
    );

  }

  /**
   * Delete the user's account
   */
  public deleteAccount(): void {
    this.modalService.dismissAll();

    this.authService.deleteCurrentUser().subscribe(
      _ => {
        this.authService.logout();
        this.alertService.success('Your user has been deleted')
      },
      err => this.handleError(err)
    )
  }

  public openDeleteModal(targetModal: any) {
    this.modalService.open(targetModal, { size: 'md', backdrop: 'static' }).result.then((result) => {}, (reason) => {});
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
    if(err.status === 403) {
      this.alertService.warn("You do not have permission to perform this action", this.optionsRouteChange);
    } else if(err.error.message) {
      this.alertService.error(err.error.message, this.optionsRouteChange);
    }
  }
}
