import { Component, OnInit } from '@angular/core';
import { FormGroup,  FormControl, AbstractControl, Validators, FormBuilder } from '@angular/forms';

import { AlertService } from '../../../services/alert/alert.service';
import { UsersService } from '../../../services/users/users.service';
import { User } from '../../../models/user';
import { PasswordMatcher } from '../../../shared/utils/password-matcher.validator';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UserCredentials } from '../../../models/user-credentials';
import { AuthService } from 'src/app/services/auth/auth.service';


@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.css']
})
export class UsersListComponent implements OnInit {

  //Form groups
  createUserForm: FormGroup;
  updateUserForm: FormGroup;
  rolesForm: FormGroup;
  passwordForm: FormGroup;

  users: User[]; // List of users

  currentUser: User; // current logged in user

  usersFiltered: User[]; // List storing the users values displayed on the screen. Its content can change dinamically depending on the searches

  searchInput: string = ""; // Variable to store the text typed by the user in the search box

  availableRoles: String[]; // List of available roles

  userSelected: number = null;

  //Pagination
  page = 1;
  pageSize = 10;
  collectionSize = 0;

  // Config options for alerts
  options = {
    autoClose: true,
    keepAfterRouteChange: false
  };

  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  constructor(private usersService: UsersService, private modalService: NgbModal, private alertService: AlertService, private formBuilder: FormBuilder, private authService: AuthService) { }

  ngOnInit(): void {

    this.getUsers(); // Get all the available users
    this.getAvailableRoles(); // Get all the available roles

    this.currentUser = this.authService.getCurrentUserValue(); // Get current logged in user

    this.createUserForm = new FormGroup({
      username: new FormControl(null, [
        Validators.required
      ]),
      email: new FormControl(null, [
        Validators.required,
        Validators.email
      ]),
      password: new FormControl(null, [
        Validators.required,
        Validators.minLength(8)
      ]),
      roles: new FormControl([], [
        Validators.required
      ])
    });

    this.updateUserForm = new FormGroup({
      username: new FormControl(null, [
        Validators.required
      ]),
      email: new FormControl(null, [
        Validators.required,
        Validators.email
      ])
    });

    this.rolesForm = new FormGroup({
      roles: new FormControl([], [
        Validators.required
      ])
    });

    this.passwordForm = this.formBuilder.group({
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
   * Get all users registered
   */
   public getUsers(): void {
    this.users = null;
    this.usersService.getAllUsers().subscribe(
      res => {
        this.users = res,
        this.collectionSize = this.users.length,
        this.usersFiltered = this.users
      },
      err => this.handleError(err)
    );
  }

  /**
   * Get all available roles
   */
   public getAvailableRoles(): void {
    this.availableRoles = null;
    this.usersService.getAllRoles().subscribe(
      res => {
        if (!this.currentUser.roles.includes('ADMIN')) {
          this.availableRoles = res.filter( item => item !== 'ADMIN')
        } else {
          this.availableRoles = res
        }
      },
      err => this.handleError(err)
    );
  }

  /**
   * Opens a modal to create a new user
   */
  public openCreateUserModal(targetModal: any): void {

    this.createUserForm.reset(); // Reset the form

    this.modalService.open(targetModal, { size: 'lg', backdrop: 'static' }).result.then((result) => {}, (reason) => {});
  }

  /**
   * Create a new user and add it to the list
   */
  public createUser(): void {

    const newUser: User = this.createUserForm.value; // Form data

    this.usersService.createUser(newUser).subscribe(
      res => {
        this.users.unshift(res); // Add the new user to the list
        this.usersFiltered = this.users, // Update the table
        this.collectionSize = this.users.length;
        this.alertService.success('User created', this.options) // Notify success
      },
      err => this.handleError(err)
    );
  }

  /**
   * Opens a modal to edit an existing user
   */
   public openUpdateUserModal(targetModal: any, index: number): void {
    this.modalService.open(targetModal, { size: 'lg', backdrop: 'static' }).result.then((result) => {}, (reason) => {
      // Reset user selected
      this.userSelected = null;

      // Reset the forms after dismiss
      this.updateUserForm.reset();
      this.rolesForm.reset();
      this.passwordForm.reset();
    });

    // Populate the modal with the selected user data
    this.userSelected = index;
    this.updateUserForm.patchValue({
      username: this.usersFiltered[index].username,
      email: this.usersFiltered[index].email
    });

    this.rolesForm.patchValue({
      roles: this.usersFiltered[index].roles
    });
  }

  /**
   * Update an existing user data
   */
  public updateUser(): void {

    const user: User = this.updateUserForm.value; // Form data

    this.usersService.updateUser(user).subscribe(
      res => {
        this.usersFiltered[this.userSelected] = res; // Update the user
        this.users[this.users.findIndex(user => user.username === this.usersFiltered[this.userSelected].username)] = res;
        this.alertService.success('User data updated', this.options) // Notify success
      },
      err => this.handleError(err)
    );
  }

  /**
   * Update an existing user roles
   */
   public updateUserRoles(): void {

    const user: User = this.usersFiltered[this.userSelected]; // User selected
    const roles: string[] = this.rolesForm.value; // Form role data

    this.usersService.updateUserRoles(user, roles).subscribe(
      res => {
        this.usersFiltered[this.userSelected].roles = res; // Update the user
        this.users[this.users.findIndex(user => user.username === this.usersFiltered[this.userSelected].username)].roles = res;
        this.alertService.success('User roles updated', this.options) // Notify success
      },
      err => this.handleError(err)
    );
  }

  /**
   * Update an existing user password
   */
   public updateUserPassword(): void {

    let credentials: UserCredentials = this.passwordForm.value; // Form data
    credentials.username = this.usersFiltered[this.userSelected].username;

    this.usersService.updateUserPassword(credentials).subscribe(
      _ => {
        this.alertService.success('User password updated', this.options) // Notify success
      },
      err => this.handleError(err)
    );
  }

  /**
   * Delete an existing user
   * @param index: position in the array of users
   */
  public deleteUser(index: number): void {

    const username = this.usersFiltered[index].username; // Get username

    this.usersService.deleteUser(username).subscribe(
      _ => {
        this.getUsers(),
        this.alertService.success('User deleted', this.options) // Notify success
      },
      err => this.handleError(err)
    );
  }

  /**
   * Method that is automatically triggered when the user types something in the search box
   * @param event: input text
   */
  public onSearch(text: string) {
    this.usersFiltered = this.users.filter(user => {
      const term = text.toLowerCase();
      return user.username.toLowerCase().includes(term) ||
      user.email.toLowerCase().includes(term);
    });

    this.collectionSize = this.usersFiltered.length; // Update the table size
  }

  /**
   * Method that is automatically triggered when changing pages, updating the value of the page
   * @param event: new page
  */
  public pageChanged(event: any): void {
    this.page = event; // New page
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
