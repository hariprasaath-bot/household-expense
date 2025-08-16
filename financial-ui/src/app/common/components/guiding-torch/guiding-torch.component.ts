import { Component, HostListener, Input  } from '@angular/core';

@Component({
  selector: 'app-guiding-torch',
  standalone: true,
  imports: [],
  templateUrl: './guiding-torch.component.html',
  styleUrl: './guiding-torch.component.scss'
})
export class GuidingTorchComponent {
  @Input() cursorX: string = '50vw';
  @Input() cursorY: string = '50vh';
  private targetX: number = window.innerWidth / 2;
  private targetY: number = window.innerHeight / 2;

  @HostListener('document:mousemove', ['$event'])
  @HostListener('document:touchmove', ['$event'])
  update(event: MouseEvent | TouchEvent): void {
    // Get the coordinates based on the event type
    const x = event instanceof MouseEvent ? event.clientX : (event.touches[0]?.clientX || 0);
    const y = event instanceof MouseEvent ? event.clientY : (event.touches[0]?.clientY || 0);
    this.targetX = x;
    this.targetY = y;

    this.moveWithInertia()
  }


  private moveWithInertia() {
    const inertiaFactor = 1.8; // Adjust for more or less inertia
    const currentX = parseFloat(this.cursorX);
    const currentY = parseFloat(this.cursorY);

    this.cursorX = `${currentX + (this.targetX - currentX) * inertiaFactor}px`;
    this.cursorY = `${currentY + (this.targetY - currentY) * inertiaFactor}px`;

    requestAnimationFrame(() => this.moveWithInertia());
  }
}
