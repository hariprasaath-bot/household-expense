import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { SupersetService } from './superset.service';
// Import the SDK
import { embedDashboard } from '@superset-ui/embedded-sdk';

@Component({
  selector: 'app-superset-embed',
  standalone: true,
  imports: [],
  templateUrl: './superset-embed.component.html',
  styleUrl: './superset-embed.component.scss'
})
export class SupersetEmbedComponent implements AfterViewInit {
  @ViewChild('supersetContainer', { static: true }) supersetContainer!: ElementRef<HTMLDivElement>;

  constructor(private supersetService: SupersetService) {}

  ngAfterViewInit() {
    this.supersetService.getGuestToken().subscribe({
      next: (token) => {
        embedDashboard({
          id: '4d2c303c-b2fb-42df-9375-30834c9e62ee',
          supersetDomain: 'http://localhost:8088',
          mountPoint: this.supersetContainer.nativeElement,
          fetchGuestToken: () => Promise.resolve(token.token),
          dashboardUiConfig: {
            hideTitle: false,
            hideChartControls: false,
          },
        });
      },
      error: (err) => {
        console.error('Failed to fetch Superset guest token', err);
      }
    });
  }
}
