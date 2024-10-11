import { Injectable, Input } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api.service';
import { Comment } from '../../models/comment';
import { CommentDTO } from '../../models/dto/comment-dto';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  @Input() datasetId: number;

  constructor(private apiService: ApiService) { }

  getCommetsByDataset(datasetId: number): Observable<Comment[]> {

    return this.apiService.get<any>('/comments?dataset=' + datasetId)
    .pipe(map(res => {
      const comments: Comment[] = res;
      return comments;
    }));
  }

  createNewComment(comment: CommentDTO): Observable<Comment> {
    return this.apiService.post<Comment>('/comments', comment)
    .pipe(map(res => {
      const newComment: Comment = res;
      return newComment;
    }));
  }

  updateComment(comment: CommentDTO): Observable<Comment> {
    return this.apiService.put<Comment>(`/comments/${comment.id}`, comment)
    .pipe(map(res => {
      const updatedComment: Comment = res;
      return updatedComment;
    }));
  }

  deleteComment(id: number): Observable<Comment>{
    return this.apiService.delete<any>(`/comments/${id}`)
    .pipe(map(res => {
      const deletedComment: Comment = res;
      return deletedComment;
    }));
  }

  purgeComment(id: number): Observable<any>{
    return this.apiService.delete<any>(`/comments/purge/${id}`);
  }
}
