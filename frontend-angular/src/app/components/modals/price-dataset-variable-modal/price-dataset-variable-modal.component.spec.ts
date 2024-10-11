import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PriceDatasetVariableModalComponent } from './price-dataset-variable-modal.component';

describe('PriceDatasetVariableModalComponent', () => {
  let component: PriceDatasetVariableModalComponent;
  let fixture: ComponentFixture<PriceDatasetVariableModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PriceDatasetVariableModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PriceDatasetVariableModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
