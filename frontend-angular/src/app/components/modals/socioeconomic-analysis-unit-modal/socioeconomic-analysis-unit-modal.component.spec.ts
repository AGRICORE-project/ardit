import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SocioeconomicAnalysisUnitModalComponent } from './socioeconomic-analysis-unit-modal.component';

describe('SocioeconomicAnalysisUnitModalComponent', () => {
  let component: SocioeconomicAnalysisUnitModalComponent;
  let fixture: ComponentFixture<SocioeconomicAnalysisUnitModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SocioeconomicAnalysisUnitModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SocioeconomicAnalysisUnitModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
