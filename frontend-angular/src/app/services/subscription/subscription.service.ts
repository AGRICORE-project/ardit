import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {

  constructor(private apiService: ApiService, private authService: AuthService) { }
  
  dtsubscribe(): Observable<any> {
    return this.apiService.post<any>('/auth/subscribe').pipe(map(res => {
      return res as string
    }))
  }

  dtunsubscribe(): Observable<any> {
    return this.apiService.post<any>('/auth/subscribe').pipe(map(res => {
      return res as string
    }))
  }

}