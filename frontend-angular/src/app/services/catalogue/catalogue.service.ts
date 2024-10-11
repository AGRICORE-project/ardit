import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api.service';
import { Catalogue } from '../../models/catalogue';

@Injectable({
  providedIn: 'root'
})
export class CatalogueService {

  constructor(private apiService: ApiService) { }

  getAllCatalogues(): Observable<Catalogue[]>{
    return this.apiService.get<any>('/datasets/catalogues')
    .pipe(map(res => {
      const catalogues: Catalogue[] = res;
      return catalogues;
    }));
  }

  getCatalogue(id: number): Observable<Catalogue>{
    return this.apiService.get<any>(`/datasets/catalogues/${id}`)
    .pipe(map(res => {
      const catalogue: Catalogue = res;
      return catalogue;
    }));
  }

  createCatalogue(catalogue: Catalogue): Observable<Catalogue>{
    return this.apiService.post<Catalogue>(`/datasets/catalogues`, catalogue)
    .pipe(map(res => {
      const newCatalogue: Catalogue = res;
      return newCatalogue;
    }));
  }

  updateCatalogue(catalogue: Catalogue): Observable<Catalogue>{
    return this.apiService.put<Catalogue>(`/datasets/catalogues/${catalogue.id}`, catalogue)
    .pipe(map(res => {
      const updatedCatalogue: Catalogue = res;
      return updatedCatalogue;
    }));
  }

  deleteCatalogue(id: number): Observable<any>{
    return this.apiService.delete<any>(`/datasets/catalogues/${id}`);
  }
}
