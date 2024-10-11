import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DataServiceModalComponent } from './data-service-modal.component';

describe('DataServiceModalComponent', () => {
  let component: DataServiceModalComponent;
  let fixture: ComponentFixture<DataServiceModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DataServiceModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DataServiceModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
