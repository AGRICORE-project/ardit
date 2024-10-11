import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api.service';
import { VocabularyValue } from '../../models/vocabulary-value';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class VocabularyValueService {

  constructor(private apiService: ApiService) { }

  getVocabularyValues(vocabularyId: number): Observable<VocabularyValue[]>{
    return this.apiService.get<any>(`/vocabularies/${vocabularyId}/values`)
    .pipe(map(res => {
      const values: VocabularyValue[] = res;
      return values;
    }));
  }

  getVocabularyValuesByTopic(topic: string): Observable<any>{
    return this.apiService.get<any>('/vocabularies' + '?topic=' + topic)
    .pipe(map(res => {
      const values: VocabularyValue[][] = [];
      res.forEach(vocabulary => {
        values.push(vocabulary);
      });
      const mergedValues = [].concat.apply([], values);
      return mergedValues;
    }));
  }

  getVocabularyValuesByExtraData(extraData: string): Observable<any>{
    return this.apiService.get<any>('/vocabularies' + '?extra_data=' + extraData)
    .pipe(map(res => {
      const values: VocabularyValue[] = res;
      return values;
    }));
  }

  getVocabularyValuesByCode(codes: string[], level: string, regionType: string): Observable<any>{

    let params = new HttpParams();

    codes.forEach(code => {
      params = params.append('code', code);
    });

    params = params.append('level', level);
    params = params.append('mappedBy', regionType);

    return this.apiService.get<any>('/vocabularies/nuts', params)
    .pipe(map(res => {
      const values: VocabularyValue[] = res;
      return values;
    }));
  }

  createVocabularyValue(vocabularyId: number, vocabularyValue: VocabularyValue): Observable<VocabularyValue>{
    return this.apiService.post<VocabularyValue>(`/vocabularies/${vocabularyId}/values`, vocabularyValue)
    .pipe(map(res => {
      const newVocabularyValue: VocabularyValue = res;
      return newVocabularyValue;
    }));
  }

  updateVocabularyValue(vocabularyId: number, valueId: number, vocabularyValue: VocabularyValue): Observable<VocabularyValue>{
    return this.apiService.put<VocabularyValue>(`/vocabularies/${vocabularyId}/values/${valueId}`, vocabularyValue)
    .pipe(map(res => {
      const updatedVocabularyValue: VocabularyValue = res;
      return updatedVocabularyValue;
    }));
  }

  deleteVocabularyValue(vocabularyId: number, valueId: number): Observable<any>{
    return this.apiService.delete<any>(`/vocabularies/${vocabularyId}/values/${valueId}`);
  }
}
