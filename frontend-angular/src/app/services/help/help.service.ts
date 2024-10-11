import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiService } from '../api/api.service';
import { typeHelp } from '../../models/help';

@Injectable({
  providedIn: 'root'
})
export class HelpService {

  constructor(private apiService: ApiService) { }

  getHelpBody(): Observable<typeHelp> {
    return this.apiService.get<typeHelp>('/help')
    .pipe(map(res => {
      const body = res;
      return body;
    }))
  }

  updateHelpBody(content: string, owner: string): Observable<string> {
    return this.apiService.post<string>('/help', {
      content,
      owner
    })
  }
}

