import { Directive, ElementRef, HostListener, Renderer2, Self, Optional } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[appCalculateInput]',
  standalone: true,
})
export class CalculateInputDirective {
  private resultElement: HTMLElement | null = null;

  constructor(
    private el: ElementRef,
    private renderer: Renderer2,
    @Optional() @Self() private ngControl: NgControl
  ) {}

  @HostListener('input', ['$event.target.value'])
  onInput(value: string) {
    const result = this.evaluate(value);
    if (result !== null) {
      this.showResult(result);
    } else {
      this.removeResultElement();
    }
  }

  @HostListener('keydown.enter', ['$event'])
  onEnter(event: KeyboardEvent) {
    event.preventDefault();
    const value = this.ngControl?.value;
    if (value) {
      const result = this.evaluate(value);
      if (result !== null) {
        const control = this.ngControl.control;
        if (control) {
          control.setValue(result);
        }
        this.removeResultElement();
      }
    }
  }

  @HostListener('blur')
  onBlur() {
    this.removeResultElement();
  }

  private evaluate(expression: string): number | null {
    if (typeof expression !== 'string') {
      return null;
    }
    // Allow numbers, operators (+, -, *, /), and decimals.
    const sanitized = expression.replace(/[^\d.\+\-\*\/]/g, '');

    // Check if the expression is valid and ends with a number
    if (sanitized === expression && /[\+\-\*\/]/.test(expression) && !/[\+\-\*\/]$/.test(expression)) {
      try {
        // Use Function constructor for safe evaluation
        const result = new Function(`return ${expression}`)();
        return isNaN(result) ? null : result;
      } catch (e) {
        // Error during evaluation (e.g., division by zero)
        return null;
      }
    }
    return null;
  }

  private showResult(result: number) {
    const parent = this.el.nativeElement.parentElement;
    if (!this.resultElement) {
      this.resultElement = this.renderer.createElement('span');
      this.renderer.addClass(this.resultElement, 'calculation-result');
      this.renderer.appendChild(parent, this.resultElement);
      this.renderer.setStyle(parent, 'position', 'relative');
    }
    this.renderer.setProperty(this.resultElement, 'innerText', `= ${result.toLocaleString()}`);
  }

  private removeResultElement() {
    if (this.resultElement) {
      this.renderer.removeChild(this.resultElement.parentElement, this.resultElement);
      this.resultElement = null;
    }
  }
}
