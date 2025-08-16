import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgressLightComponent } from './progress-light.component';

describe('ProgressLightComponent', () => {
  let component: ProgressLightComponent;
  let fixture: ComponentFixture<ProgressLightComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgressLightComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProgressLightComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
