import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionsRootComponent } from './transactions-root.component';

describe('TransactionsRootComponent', () => {
  let component: TransactionsRootComponent;
  let fixture: ComponentFixture<TransactionsRootComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionsRootComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionsRootComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
