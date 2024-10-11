import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DistributionTableComponent } from './distribution-table.component';

describe('DistributionTableComponent', () => {
  let component: DistributionTableComponent;
  let fixture: ComponentFixture<DistributionTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DistributionTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DistributionTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
