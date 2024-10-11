import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DistributionDetailsModalComponent } from './distribution-details-modal.component';

describe('DistributionDetailsModalComponent', () => {
  let component: DistributionDetailsModalComponent;
  let fixture: ComponentFixture<DistributionDetailsModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DistributionDetailsModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DistributionDetailsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
