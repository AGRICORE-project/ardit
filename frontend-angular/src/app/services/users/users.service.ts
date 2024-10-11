import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api.service';
import { User } from '../../models/user';
import { UserCredentials } from '../../models/user-credentials';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private apiService: ApiService) { }

  getAllUsers(): Observable<User[]>{
    return this.apiService.get<any>('/users')
    .pipe(map(res => {
      const users: User[] = res;
      return users;
    }));
  }

  getAllRoles(): Observable<String[]>{
    return this.apiService.get<any>('/users/roles')
    .pipe(map(res => {
      const roles: String[] = res;
      return roles;
    }));
  }

  getUser(username: string): Observable<User>{
    return this.apiService.get<any>(`/users/${username}`)
    .pipe(map(res => {
      const user: User = res;
      return user;
    }));
  }

  createUser(user: User): Observable<User>{
    return this.apiService.post<User>(`/users`, user)
    .pipe(map(res => {
      const newUser: User = res;
      return newUser;
    }));
  }

  updateUser(user: User): Observable<User>{
    return this.apiService.put<User>(`/users/${user.username}`, user)
    .pipe(map(res => {
      const updatedUser: User = res;
      return updatedUser;
    }));
  }

  updateUserPassword(credentials: UserCredentials): Observable<User>{
    return this.apiService.put<User>(`/users/${credentials.username}/password`, credentials)
    .pipe(map(res => {
      const updatedUser: User = res;
      return updatedUser;
    }));
  }

  updateUserRoles(user: User, roles: string[]): Observable<string[]>{
    return this.apiService.put<string[]>(`/users/${user.username}/roles`, roles)
    .pipe(map(res => {
      const newRoles: string[] = res;
      return newRoles;
    }));
  }

  deleteUser(username: string): Observable<any>{
    return this.apiService.delete<any>(`/users/${username}`);
  }
}
