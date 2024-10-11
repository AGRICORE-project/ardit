import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewCommentBoxComponent } from './new-comment-box.component';

describe('NewCommentBoxComponent', () => {
  let component: NewCommentBoxComponent;
  let fixture: ComponentFixture<NewCommentBoxComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewCommentBoxComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewCommentBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
