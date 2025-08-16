import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuidingTorchComponent } from './guiding-torch.component';

describe('GuidingTorchComponent', () => {
  let component: GuidingTorchComponent;
  let fixture: ComponentFixture<GuidingTorchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GuidingTorchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GuidingTorchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
