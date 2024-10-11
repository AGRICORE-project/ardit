import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VocabularyCreateComponent } from './vocabulary-create.component';

describe('VocabularyCreateComponent', () => {
  let component: VocabularyCreateComponent;
  let fixture: ComponentFixture<VocabularyCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VocabularyCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VocabularyCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
