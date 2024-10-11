import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl, Validators, AbstractControl } from '@angular/forms';
import { CommentDTO } from '../../../models/dto/comment-dto';

@Component({
  selector: 'app-new-comment-box',
  templateUrl: './new-comment-box.component.html',
  styleUrls: ['./new-comment-box.component.css']
})
export class NewCommentBoxComponent implements OnInit {

  @Input() datasetId: number;
  @Output() newComment: EventEmitter<CommentDTO> = new EventEmitter();

  commentForm: FormGroup;

  constructor() { }

  ngOnInit(): void {

    this.commentForm = new FormGroup({
      content: new FormControl([], [
        Validators.maxLength(500)
      ])
    });
  }

  public submitNewComment(): void {

    let newComment: CommentDTO = this.commentForm.value;
    newComment.parentId = null;
    newComment.datasetId = this.datasetId;
    this.newComment.emit(newComment);
    this.commentForm.reset();
  }

  /**
    * Form validation helpers
  */
   public fieldInvalid(form: FormGroup, fieldName: string): boolean {
    const control: AbstractControl = form.controls[fieldName];
    const invalid: boolean = control.invalid;
    const dirty: boolean = control.dirty;
    const touched: boolean = control.touched;
    return invalid && (dirty || touched);
  }

}
