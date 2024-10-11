import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { Observable ,  throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})

export class ApiService {

    private apiUrl = `${environment.APIArditEndpoint}`;

    private httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json'})
      };

    constructor(private http: HttpClient) { }

    get<T>(path: string, params: HttpParams = new HttpParams()): Observable<T> {

        let customHttpOptions = {
            headers: new HttpHeaders({ 'Content-Type': 'application/json'}),
            params: params
          };

        return this.http.get<T>(`${this.apiUrl}${path}`, customHttpOptions)
            .pipe(catchError(this.onError));
    }

    post<T>(path: string, body: object = {}): Observable<T> {
        return this.http.post<T>(`${this.apiUrl}${path}`, body, this.httpOptions)
            .pipe(catchError(this.onError));
    }

    put<T>(path: string, body: object = {}): Observable<T> {
        return this.http.put<T>(`${this.apiUrl}${path}`, body, this.httpOptions)
            .pipe(catchError(this.onError));
    }

    delete<T>(path: string, body: object = {}): Observable<T> {
        return this.http.delete<T>(`${this.apiUrl}${path}`, this.httpOptions)
            .pipe(catchError(this.onError));
    }

    private onError(error: any): Observable<never> {
        return throwError(error);
    }
}
