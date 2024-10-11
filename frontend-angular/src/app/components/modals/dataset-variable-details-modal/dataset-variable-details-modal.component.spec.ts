import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatasetVariableDetailsModalComponent } from './dataset-variable-details-modal.component';

describe('DatasetVariableDetailsModalComponent', () => {
  let component: DatasetVariableDetailsModalComponent;
  let fixture: ComponentFixture<DatasetVariableDetailsModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatasetVariableDetailsModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatasetVariableDetailsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
