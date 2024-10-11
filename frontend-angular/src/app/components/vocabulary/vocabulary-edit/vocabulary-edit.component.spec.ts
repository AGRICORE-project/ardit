import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VocabularyEditComponent } from './vocabulary-edit.component';

describe('VocabularyEditComponent', () => {
  let component: VocabularyEditComponent;
  let fixture: ComponentFixture<VocabularyEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VocabularyEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VocabularyEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
