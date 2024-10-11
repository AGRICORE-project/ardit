import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VariableTableComponent } from './variable-table.component';

describe('VariableTableComponent', () => {
  let component: VariableTableComponent;
  let fixture: ComponentFixture<VariableTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VariableTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VariableTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
