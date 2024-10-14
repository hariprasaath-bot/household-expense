import { TestBed } from '@angular/core/testing';

import { ProgressLightServiceService } from './progress-light-service.service';

describe('ProgressLightServiceService', () => {
  let service: ProgressLightServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProgressLightServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
