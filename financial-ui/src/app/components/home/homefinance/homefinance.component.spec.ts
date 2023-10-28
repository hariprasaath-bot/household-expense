import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomefinanceComponent } from './homefinance.component';

describe('HomefinanceComponent', () => {
  let component: HomefinanceComponent;
  let fixture: ComponentFixture<HomefinanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HomefinanceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomefinanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
