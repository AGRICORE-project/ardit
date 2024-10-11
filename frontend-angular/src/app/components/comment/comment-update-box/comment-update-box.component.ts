import { Component, Input, OnInit } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Comment } from '../../../models/comment';
import { FormGroup, FormControl, Validators, AbstractControl } from '@angular/forms';
import { CommentDTO } from '../../../models/dto/comment-dto';
import { CommentService } from '../../../services/comment/comment.service';
import { Observable } from 'rxjs';


@Component({
  selector: 'app-comment-update-box',
  templateUrl: './comment-update-box.component.html',
  styleUrls: ['./comment-update-box.component.css']
})
export class CommentUpdateBoxComponent implements OnInit {
  
    @Input() commentSelected: Comment; // Comment to update
    @Input() datasetId: number; // Id of the dataset
    // Form groups
    updateForm: FormGroup;
  
    constructor(public activeModal: NgbActiveModal) { }
  
    ngOnInit(): void {
      this.updateForm = new FormGroup({
        content: new FormControl([this.commentSelected.content], [
          Validators.maxLength(500)
        ])
      });
  
    }
  
    /**
     * Submit the form and update the comment
    */
     public submitUpdate(): void {
      const comment: CommentDTO = {
        id: this.commentSelected.id,
        content: this.updateForm.value.content,
        parentId: this.commentSelected.parentId,
        rootId: this.commentSelected.rootId,
        datasetId: this.datasetId
      } 
      this.updateForm.reset();
      this.activeModal.close(comment)
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
  