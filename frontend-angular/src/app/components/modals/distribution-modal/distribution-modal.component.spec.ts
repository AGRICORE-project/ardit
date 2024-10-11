import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DistributionModalComponent } from './distribution-modal.component';

describe('DistributionModalComponent', () => {
  let component: DistributionModalComponent;
  let fixture: ComponentFixture<DistributionModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DistributionModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DistributionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
