import { Injectable } from '@angular/core';
import { User } from '../../models/user';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Router } from '@angular/router';

import { ApiService } from '../api/api.service';
import { RoutesNames } from '../../shared/utils/routes.names';
import { CurrentUserCredentials } from '../../models/current-user-credentials';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private token;

  private currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  // Config options for alerts
  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  constructor(private router: Router, private apiService: ApiService) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  signIn(username: string, password: string): Observable<any>{
    return this.apiService.post('/auth/login', {username: username, password: password})
    .pipe(map(token => {
      this.token = token;
      return token;
    }));
  }

  signUp(username: string, email: string, password: string): Observable<any> {
    return this.apiService.post<any>('/auth/register', {username: username, email: email, password: password})
    .pipe(map(res => {
      return res as string;
    }));
  }

  confirmAccount(token: string): Observable<string>{

    let params = new HttpParams();
    params = params.append('token', token.toString());

    return this.apiService.get<any>('/auth/register/confirm', params)
    .pipe(map(res => {
      return res as string;
    }));
  }

  logout(): Observable<any> {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate([`/${RoutesNames.LOGIN}`]);

    return this.apiService.post<any>('/auth/logout', null);
  }

  recoverPassword(email): Observable<any> {
    return this.apiService.post<any>(`/auth/recoverPassword`, { email })
    .pipe(map(res => { 
      return res as string
    }))
  }

  resetPassword(token: string, password: string): Observable<any> {
    return this.apiService.post<any>(`/auth/newPassword`, { resetPasswordToken: token, newPassword: password })
    .pipe(map(res => { 
      return res as string
    }))
  }

  // TODO: check this method to avoid duplications
  getCurrentUser(): Observable<User>{
    return this.apiService.get<any>('/auth/me')
    .pipe(map(user => {
      if (user) {
        // Store user data in local storage
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
      }
      return user;
    }));
  }

  getUser(): Observable<User>{
    return this.apiService.get<any>('/auth/me')
    .pipe(map(res => {
      const user: User = res;
      return user;
    }));
  }

  updateCurrentUser(user: User): Observable<User>{
    return this.apiService.put<User>('/auth/me', user)
    .pipe(map(res => {
      const updatedUser: User = res;
      return updatedUser;
    }));
  }

  updateCurrentUserPassword(credentials: CurrentUserCredentials): Observable<User>{
    return this.apiService.put<User>('/auth/me/password', credentials)
    .pipe(map(res => {
      const updatedUser: User = res;
      return updatedUser;
    }));
  }

  deleteCurrentUser(): Observable<any>{
    return this.apiService.delete<any>('/auth/me');
  }

  public getCurrentUserValue(): User {
    return this.currentUserSubject.value;
  }
}
