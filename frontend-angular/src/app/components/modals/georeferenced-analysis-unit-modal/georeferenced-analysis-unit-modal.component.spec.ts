import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GeoreferencedAnalysisUnitModalComponent } from './georeferenced-analysis-unit-modal.component';

describe('GeoreferencedAnalysisUnitModalComponent', () => {
  let component: GeoreferencedAnalysisUnitModalComponent;
  let fixture: ComponentFixture<GeoreferencedAnalysisUnitModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GeoreferencedAnalysisUnitModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GeoreferencedAnalysisUnitModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
