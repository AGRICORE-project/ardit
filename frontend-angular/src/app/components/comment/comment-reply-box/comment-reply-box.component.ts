import { Component, Input, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Comment } from '../../../models/comment';
import { FormGroup, FormControl, Validators, AbstractControl } from '@angular/forms';
import { CommentDTO } from '../../../models/dto/comment-dto';

@Component({
  selector: 'app-comment-reply-box',
  templateUrl: './comment-reply-box.component.html',
  styleUrls: ['./comment-reply-box.component.css']
})
export class CommentReplyBoxComponent implements OnInit {

  @Input() commentSelected: Comment; // Comment to reply

  // Form groups
  replyForm: FormGroup;

  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit(): void {

    this.replyForm = new FormGroup({
      content: new FormControl([], [
        Validators.maxLength(500)
      ])
    });

  }

  /**
   * Retrieve the values inserted in the form and return them to the parent component
  */
   public submitReply(): void {
    let reply: CommentDTO = this.replyForm.value; // Get form value
    reply.parentId = this.commentSelected.id;
    reply.rootId = this.commentSelected.rootId;

    this.replyForm.reset(); // Reset the form

    this.activeModal.close(reply); // Send the reply back to the parent component
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
