import { TestBed } from '@angular/core/testing';

import { VocabularyValueService } from './vocabulary-value.service';

describe('VocabularyValueService', () => {
  let service: VocabularyValueService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VocabularyValueService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
