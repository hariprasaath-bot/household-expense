import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoilingEffectComponent } from './boiling-effect.component';

describe('BoilingEffectComponent', () => {
  let component: BoilingEffectComponent;
  let fixture: ComponentFixture<BoilingEffectComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BoilingEffectComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BoilingEffectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
