import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransactionUploadComponent } from './transaction-upload.component';

describe('TransactionUploadComponent', () => {
  let component: TransactionUploadComponent;
  let fixture: ComponentFixture<TransactionUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransactionUploadComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransactionUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
