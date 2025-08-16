import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProgressLightServiceService  {
  loadingSubject = new BehaviorSubject<boolean>(false);
  progressSubject = new BehaviorSubject<number>(0);
  interval: any;

  loading$ = this.loadingSubject.asObservable();
  progress$ = this.progressSubject.asObservable();

  startLoading() {
    this.loadingSubject.next(true);
    this.progressSubject.next(0);
  }

  setProgress(value: number) {
    this.progressSubject.next(value);
  }

  stopLoading() {
    this.loadingSubject.next(false);
    this.progressSubject.next(0); // Reset progress
    this.clearInterval(); // Clear interval when loading stops
  }

  clearInterval() {
    if (this.interval) {
      clearInterval(this.interval);
      this.interval = null;
    }
  }

  decreaseProgress() {
    const currentProgress = this.progressSubject.value;
    if (currentProgress > 0) {
      this.setProgress(currentProgress - 5); // Decrease progress
    }
  }
}