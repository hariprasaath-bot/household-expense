import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignUpFormsComponent } from './sign-up-forms.component';

describe('SignUpFormsComponent', () => {
  let component: SignUpFormsComponent;
  let fixture: ComponentFixture<SignUpFormsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SignUpFormsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SignUpFormsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
