import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatasetVariableModalComponent } from './dataset-variable-modal.component';

describe('DatasetVariableModalComponent', () => {
  let component: DatasetVariableModalComponent;
  let fixture: ComponentFixture<DatasetVariableModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatasetVariableModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatasetVariableModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
