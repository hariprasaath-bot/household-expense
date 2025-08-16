import { Component } from '@angular/core';
import { ProgressLightServiceService } from '../../service/progress-light-service.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-progress-light',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './progress-light.component.html',
  styleUrl: './progress-light.component.scss'
})
export class ProgressLightComponent {
  isLoading: boolean = false;
  progress: number = 0;

  constructor(private progressService: ProgressLightServiceService) {}

  ngOnInit(): void {
    this.progressService.loading$.subscribe(loading => {
      this.isLoading = loading;
    });

    this.progressService.progress$.subscribe(value => {
      this.progress = value;
    });
  }
}
