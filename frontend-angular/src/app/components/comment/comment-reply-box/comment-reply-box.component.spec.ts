import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentReplyBoxComponent } from './comment-reply-box.component';

describe('CommentReplyBoxComponent', () => {
  let component: CommentReplyBoxComponent;
  let fixture: ComponentFixture<CommentReplyBoxComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CommentReplyBoxComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentReplyBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
