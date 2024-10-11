import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalysisUnitDetailsModalComponent } from './analysis-unit-details-modal.component';

describe('AnalysisUnitDetailsModalComponent', () => {
  let component: AnalysisUnitDetailsModalComponent;
  let fixture: ComponentFixture<AnalysisUnitDetailsModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnalysisUnitDetailsModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalysisUnitDetailsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
