import { Injectable } from '@angular/core';
import { ApiService } from '../api/api.service';
import { Observable } from 'rxjs';
import { EmailDTO } from '../../models/dto/email-dto';

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  constructor(private ApiService: ApiService) { }

  sendEmail( email: EmailDTO ): Observable<any> {
    return this.ApiService.post(`/contact`, email)
  }

}
