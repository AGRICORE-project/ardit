import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api.service';
import { Vocabulary } from '../../models/vocabulary';

@Injectable({
  providedIn: 'root'
})
export class VocabularyService {

  constructor(private apiService: ApiService) { }

  getAllVocabularies(): Observable<Vocabulary[]>{
    return this.apiService.get<any>('/vocabularies')
    .pipe(map(res => {
      const vocabularies: Vocabulary[] = res;
      return vocabularies;
    }));
  }

  getVocabulary(id: number): Observable<Vocabulary>{
    return this.apiService.get<any>(`/vocabularies/${id}`)
    .pipe(map(res => {
      const vocabulary: Vocabulary = res;
      return vocabulary;
    }));
  }

  getVocabularyTopics(): Observable<String[]>{
    return this.apiService.get<any>(`/vocabularies/topics`)
    .pipe(map(res => {
      const topics: String[] = res;
      return topics;
    }));
  }

  createVocabulary(vocabulary: Vocabulary): Observable<Vocabulary>{
    return this.apiService.post<Vocabulary>(`/vocabularies`, vocabulary)
    .pipe(map(res => {
      const newVocabulary: Vocabulary = res;
      return newVocabulary;
    }));
  }

  updateVocabulary(vocabulary: Vocabulary): Observable<Vocabulary>{
    return this.apiService.put<Vocabulary>(`/vocabularies/${vocabulary.id}`, vocabulary)
    .pipe(map(res => {
      const updatedVocabulary: Vocabulary = res;
      return updatedVocabulary;
    }));
  }

  deleteVocabulary(id: number): Observable<any>{
    return this.apiService.delete<any>(`/vocabularies/${id}`);
  }
}
