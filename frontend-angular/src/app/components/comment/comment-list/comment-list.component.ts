import { Component, OnInit, Input } from '@angular/core';

import { CommentService } from '../../../services/comment/comment.service';
import { AlertService } from '../../../services/alert/alert.service';
import { Comment } from '../../../models/comment';
import { CommentDTO } from '../../../models/dto/comment-dto';
import { CommentReplyBoxComponent } from '../comment-reply-box/comment-reply-box.component';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { __assign } from 'tslib';
import { User } from 'src/app/models/user';
import { AuthService } from 'src/app/services/auth/auth.service';
import { CommentUpdateBoxComponent } from '../comment-update-box/comment-update-box.component';

@Component({
  selector: 'app-comment-list',
  templateUrl: './comment-list.component.html',
  styleUrls: ['./comment-list.component.css']
})
export class CommentListComponent implements OnInit {

  @Input() datasetId: number;

  emptyComment: boolean = true;

  parentSelected: number = null;
  childLevel1Selected: number = null;
  childLevel2Selected: number = null;

  comments: Comment[]; // List of comments

  currentUser: User;

  // Config options for alerts
  options = {
    autoClose: true,
    keepAfterRouteChange: false
  };

  optionsRouteChange = {
    autoClose: true,
    keepAfterRouteChange: true
  };

  constructor(private commentService: CommentService, private alertService: AlertService, private authService: AuthService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUserValue();
    this.getComments();
  }

  public getComments(): void {

    this.commentService.getCommetsByDataset(this.datasetId).subscribe(
      res => {
        this.comments = res
      },
      err => this.handleError(err)
    );
  }

  /**
   * Send a new comment (create a new thread of comments)
   * @param comment: new comment to be sent
   */
  public sendNewComment(comment: CommentDTO) {
    this.commentService.createNewComment(comment).subscribe(
      res => {
        this.comments = [res, ...this.comments],
        this.alertService.success("Comment added", this.options);
      },
      err => this.handleError(err)
    );
  }

  /**
   * Send a new reply to an existing comment
   * @param comment: reply to be sent
   */
  public sendReply(comment: CommentDTO) {

    comment.datasetId = this.datasetId;

    this.commentService.createNewComment(comment).subscribe(
      res => {
        if(this.childLevel1Selected != null) {

          this.comments[this.parentSelected].replies[this.childLevel1Selected].replies = [res, ...this.comments[this.parentSelected].replies[this.childLevel1Selected].replies];
        
        } else if(this.parentSelected!= null && this.childLevel1Selected == null) {

          this.comments[this.parentSelected].replies = [res, ...this.comments[this.parentSelected].replies];
        }
        this.alertService.success("Reply added", this.options);
      },
      err => this.handleError(err)
    );
  }

  /**
   * Opens a modal to create a new reply to the comment
   * @param commentIndex: root comment index
   * @param replyLevel1Index: level 1 reply index
   * @param replyLevel2Index: level 2 reply index
   */
   createReply(commentIndex: number, replyLevel1Index: number = null, replyLevel2Index: number = null): void {

    const modal = this.modalService.open(CommentReplyBoxComponent, { size: 'lg', backdrop: 'static' });
   
    this.parentSelected = commentIndex;
    this.childLevel1Selected = replyLevel1Index;
    this.childLevel2Selected = replyLevel2Index;

    if (replyLevel1Index != null && replyLevel2Index == null) {
      modal.componentInstance.commentSelected = this.comments[commentIndex].replies[replyLevel1Index];
    } else if (replyLevel1Index != null && replyLevel2Index != null) {
      modal.componentInstance.commentSelected = this.comments[commentIndex].replies[replyLevel1Index].replies[replyLevel2Index];
    } else {
      modal.componentInstance.commentSelected = this.comments[commentIndex];
    }

    // Submit the reply
    modal.result.then((result) => {
      this.sendReply(result);
    }, (reason) => {
      this.parentSelected = null;
      this.childLevel1Selected = null;
      this.childLevel2Selected = null;
    });
  }

    /**
   * Update comment
   * @param commentIndex: root comment index
   * @param replyLevel1Index: level 1 reply index
   * @param replyLevel2Index: level 2 reply index
   */
   updateComment(comment: CommentDTO, commentIndex, replyLevel1Index, replyLevel2Index, level): void {
      this.commentService.updateComment(comment).subscribe(
        res => {
          switch(level) { 
            case 0: {
              if (res) {
                res.replies =  this.comments[commentIndex].replies;
                this.comments[commentIndex] = res;
              }
              break; 
            }
            case 1: {
              res.replies = this.comments[commentIndex].replies[replyLevel1Index].replies;
              this.comments[commentIndex].replies[replyLevel1Index] = res;
              break; 
            }
            case 2: {
              this.comments[commentIndex].replies[replyLevel1Index].replies[replyLevel2Index] = res;
              break; 
            }
          }
          this.alertService.success("Comment updated", this.options);
        },
        err => this.handleError(err)
      )
    }

    /**
   * Opens a modal to update a comment
   * @param commentIndex: root comment index
   * @param replyLevel1Index: level 1 reply index
   * @param replyLevel2Index: level 2 reply index
   */
     modalUpdate(commentIndex: number, replyLevel1Index: number = null, replyLevel2Index: number = null, level: number): void {

      const modal = this.modalService.open(CommentUpdateBoxComponent, { size: 'lg', backdrop: 'static' });
      modal.componentInstance.datasetId = this.datasetId;

      this.parentSelected = commentIndex;
      this.childLevel1Selected = replyLevel1Index;
      this.childLevel2Selected = replyLevel2Index;

      if (replyLevel1Index != null && replyLevel2Index == null) {
        modal.componentInstance.commentSelected = this.comments[commentIndex].replies[replyLevel1Index];
      } else if (replyLevel1Index != null && replyLevel2Index != null) {
        modal.componentInstance.commentSelected = this.comments[commentIndex].replies[replyLevel1Index].replies[replyLevel2Index];
      } else {
        modal.componentInstance.commentSelected = this.comments[commentIndex];
      }

      modal.result.then((result) => {
        this.updateComment(result, commentIndex, replyLevel1Index ,replyLevel2Index, level)
      }, (reason) => {
        this.parentSelected = null;
        this.childLevel1Selected = null;
        this.childLevel2Selected = null;
      });
    }

  /**
   * Delete the comment (only authors)
   * Changes the author and the content to "This comment was deleted"
   * @param commentIndex: root comment index
   * @param replyLevel1Index: level 1 reply index
   * @param replyLevel2Index: level 2 reply index
   * @param level: level [0, 1, 2]
   */
  deleteComment(commentIndex: number, replyLevel1Index: number = null, replyLevel2Index: number = null, level: number): void {

    let commentId: number;

    switch(level) { 
      case 0: {
        commentId = this.comments[commentIndex].id;
        break; 
      }
      case 1: {
        commentId = this.comments[commentIndex].replies[replyLevel1Index].id;
        break; 
      }
      case 2: {
        commentId = this.comments[commentIndex].replies[replyLevel1Index].replies[replyLevel2Index].id;
        break; 
      }
    }

    this.commentService.deleteComment(commentId).subscribe(
      res => {

        switch(level) { 
          case 0: {
            if (res) {
              res.replies =  this.comments[commentIndex].replies;
              this.comments[commentIndex] = res;
            } else {
              // Response is null when comment to be deleted is root and does not have replies, remove the comment from the list
              this.comments.splice(commentIndex, 1);
            }
            
            break; 
          }
          case 1: {
            res.replies = this.comments[commentIndex].replies[replyLevel1Index].replies;
            this.comments[commentIndex].replies[replyLevel1Index] = res;
            break; 
          }
          case 2: {
            this.comments[commentIndex].replies[replyLevel1Index].replies[replyLevel2Index] = res;
            break; 
          }
        }
        
        this.alertService.success("Comment deleted", this.options);
      },
      err => this.handleError(err)
    );

  }

  /**
   * Purge the comment (only admins)
   * Purge the comment from the database and the lists
   * @param commentIndex: root comment index
   * @param replyLevel1Index: level 1 reply index
   * @param replyLevel2Index: level 2 reply index
   * @param level: level [0, 1, 2]
   */
  purgeComment(commentIndex: number, replyLevel1Index: number = null, replyLevel2Index: number = null, level: number): void {
    let commentId: number;

    switch(level) { 
      case 0: {
        commentId = this.comments[commentIndex].id;
        break; 
      }
      case 1: {
        commentId = this.comments[commentIndex].replies[replyLevel1Index].id;
        break; 
      }
      case 2: {
        commentId = this.comments[commentIndex].replies[replyLevel1Index].replies[replyLevel2Index].id;
        break; 
      }
    }

    this.commentService.purgeComment(commentId).subscribe(
      _ => {

        switch(level) { 
          
          case 0: {
            // Delete root comment and replies at level 1 and 2
            for (let i=0; i<this.comments[commentIndex].replies.length; i++) {

              for (let j=0; j<this.comments[commentIndex].replies[i].replies.length; j++) {
                // Level 2 replies
                this.comments[commentIndex].replies[i].replies.splice(j, 1);
              }

              // Level 1 replies
              this.comments[commentIndex].replies.splice(i, 1);
            }

            // Comment to be deleted
            this.comments.splice(commentIndex, 1);
            break; 
          }

          case 1: {
            // Delet replies at level 2
            for (let i=0; i<this.comments[commentIndex].replies[replyLevel1Index].replies.length; i++) {
              this.comments[commentIndex].replies[replyLevel1Index].replies.splice(i, 1);
            }

            // Comment to be deleted at level 1
            this.comments[commentIndex].replies.splice(replyLevel1Index, 1);
            break; 
          }

          case 2: {
            // Comment to be deleted at level 2
            this.comments[commentIndex].replies[replyLevel1Index].replies.splice(replyLevel2Index, 1);
            break; 
          }
        }
        
        this.alertService.success("Comment deleted", this.options);
      },
      err => this.handleError(err)
    );
  }

  handleError(err: any): any{
    if(err.status === 403) {
      this.alertService.warn("You do not have permission to perform this action", this.optionsRouteChange);
    } else if(err.error.message) {
      this.alertService.error(err.error.message, this.optionsRouteChange);
    }
  }

}
