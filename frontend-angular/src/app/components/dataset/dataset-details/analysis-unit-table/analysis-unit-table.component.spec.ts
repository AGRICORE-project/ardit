import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalysisUnitTableComponent } from './analysis-unit-table.component';

describe('AnalysisUnitTableComponent', () => {
  let component: AnalysisUnitTableComponent;
  let fixture: ComponentFixture<AnalysisUnitTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnalysisUnitTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalysisUnitTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
