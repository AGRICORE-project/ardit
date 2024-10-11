import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { HttpParams } from '@angular/common/http';

import { ApiService } from '../api/api.service';
import { Dataset } from '../../models/dataset';
import { DatasetDescription } from '../../models/dataset-description';
import { Keyword } from '../../models/keyword';
import { PageableElement } from '../../models/pageable-element';
import { Catalogue } from '../../models/catalogue';
import { DatasetDTO } from '../../models/dto/dataset-dto';

@Injectable({
  providedIn: 'root'
})
export class DatasetService {

  constructor(private apiService: ApiService) { }

  getDatasetsByFilters(page: number = 1, queryParams: Map<string, string> = null): Observable<PageableElement>{

    let params = new HttpParams();

    // Append the params to the route if they are not null
    params = params.append('page', page.toString()); // Page param is always sent
    
    if (queryParams) {
      if(queryParams.has("query")) params = params.append('query', queryParams.get('query'));
      if(queryParams.has("type") && queryParams.get("type")!="all") params = params.append('type', queryParams.get("type")); //If type is 'all', do not add it to the request, because, by default, it will search for both types
      if(queryParams.has("draft") && queryParams.get("draft")!="all") params = params.append('draft', queryParams.get("draft"));
      if(queryParams.has("task")) params = params.append('task', queryParams.get("task"));
      if(queryParams.has("producer")) params = params.append('producer', queryParams.get("producer"));
      if(queryParams.has("catalogue")) params = params.append('catalogue',queryParams.get("catalogue"));
      if(queryParams.has("analysisUnit")) params = params.append('analysisUnit', queryParams.get("analysisUnit"));
      if(queryParams.has("variable")) params = params.append('variable', queryParams.get("variable"));
      if(queryParams.has("from")) params = params.append('from', queryParams.get("from"));
      if(queryParams.has("to")) params = params.append('to', queryParams.get("to"));
      if(queryParams.has("language")) params = params.append('language', queryParams.get("language"));
      if(queryParams.has("periodicity")) params = params.append('periodicity', queryParams.get("periodicity"));
      if(queryParams.has("format")) params = params.append('format', queryParams.get("format"));
      if(queryParams.has("continent")) params = params.append('continent', queryParams.get("continent"));
      if(queryParams.has("country")) params = params.append('country', queryParams.get("country"));
      if(queryParams.has("nuts1")) params = params.append('nuts1', queryParams.get("nuts1"));
      if(queryParams.has("nuts2")) params = params.append('nuts2', queryParams.get("nuts2"));
      if(queryParams.has("nuts3")) params = params.append('nuts3', queryParams.get("nuts3"));
      if(queryParams.has("sortBy")) params = params.append('sortBy', queryParams.get("sortBy"));
    }

    return this.apiService.get<any>('/datasets/search', params)
    .pipe(map(res => {
      const datasets: PageableElement = res;
      return datasets;
    }));
  }

  getDatasetDTO(id: number): Observable<DatasetDTO>{    
    return this.apiService.get<any>(`/datasets/search/${id}`)
    .pipe(map(res => {
      const dataset: DatasetDTO = res;
      return dataset;
    }));
  }

  getDataset(id: number): Observable<Dataset>{
    return this.apiService.get<any>(`/datasets/${id}`)
    .pipe(map(res => {
      const dataset: Dataset = res;
      return dataset;
    }));
  }

  getDatasetsByCatalogue(catalogueId: number, page: number = 1): Observable<PageableElement>{

    let params = new HttpParams();
    params = params.append('page', page.toString());
    return this.apiService.get<any>('/datasets/search' + '?catalogue=' + catalogueId, params)
    .pipe(map(res => {
      const datasets: PageableElement = res;
      return datasets;
    }));
  }

  getDatasetTypes(): Observable<String[]>{
    return this.apiService.get<any>(`/datasets/types`)
    .pipe(map(res => {
      const types: String[] = res;
      return types;
    }));
  }

  createDataset(dataset: Dataset): Observable<Dataset>{
    return this.apiService.post<Dataset>(`/datasets`, dataset)
    .pipe(map(res => {
      const newDataset: Dataset = res;
      return newDataset;
    }));
  }

  updateDataset(dataset: Dataset): Observable<Dataset>{
    return this.apiService.put<Dataset>(`/datasets/${dataset.id}`, dataset)
    .pipe(map(res => {
      const updatedDataset: Dataset = res;
      return updatedDataset;
    }));
  }

  duplicateDataset(dataset: Dataset): Observable<Dataset>{
    return this.apiService.post<Dataset>(`/datasets/duplicate`, dataset)
    .pipe(map(res => {
      const newDataset: Dataset = res;
      return newDataset;
    }));
  }

  deleteDataset(id: number): Observable<any>{
    return this.apiService.delete<any>(`/datasets/${id}`);
  }

  getPropertiesDescriptions(): Observable<DatasetDescription[]>{
    return this.apiService.get<any>('/datasets/descriptions')
    .pipe(map(res => {
      const datasetDescriptions: DatasetDescription[] = res;
      return datasetDescriptions;
    }));
  }

  updatePropertyDescription(id: number, description: any): Observable<string>{
    return this.apiService.put<any>(`/datasets/descriptions/${id}`, description)
    .pipe(map(res => {
      const descriptionDeleted: string = res;
      return descriptionDeleted;
    }));
  }

  deletePropertyDescription(id: number): Observable<string>{
    return this.apiService.delete<any>(`/datasets/descriptions/${id}`)
    .pipe(map(res => {
      const descriptionDeleted: string = res;
      return descriptionDeleted;
    }));
  }

  getKeywordsByTitle(label: string): Observable<any>{
    return this.apiService.get<any>('/datasets/keywords' + '?query=' + label)
    .pipe(map(res => {
      const keywords: Keyword[] = res;
      return keywords;
    }));
  }
  
}
