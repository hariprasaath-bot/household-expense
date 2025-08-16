import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupersetEmbedComponent } from './superset-embed.component';

describe('SupersetEmbedComponent', () => {
  let component: SupersetEmbedComponent;
  let fixture: ComponentFixture<SupersetEmbedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupersetEmbedComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupersetEmbedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
