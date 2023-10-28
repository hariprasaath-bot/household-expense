import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonalfinanceComponent } from './personalfinance.component';

describe('PersonalfinanceComponent', () => {
  let component: PersonalfinanceComponent;
  let fixture: ComponentFixture<PersonalfinanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PersonalfinanceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PersonalfinanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
