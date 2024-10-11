import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatasetDescriptionsComponent } from './dataset-descriptions.component';

describe('DatasetDescriptionsComponent', () => {
  let component: DatasetDescriptionsComponent;
  let fixture: ComponentFixture<DatasetDescriptionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatasetDescriptionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatasetDescriptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
