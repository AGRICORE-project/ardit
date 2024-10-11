import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VocabularyValueModalComponent } from './vocabulary-value-modal.component';

describe('VocabularyValueModalComponent', () => {
  let component: VocabularyValueModalComponent;
  let fixture: ComponentFixture<VocabularyValueModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VocabularyValueModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VocabularyValueModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
