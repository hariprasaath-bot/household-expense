import { Component } from '@angular/core';
import { RippleModule } from 'primeng/ripple';

@Component({
  selector: 'app-transactions-root',
  standalone: true,
  imports: [RippleModule],
  templateUrl: './transactions-root.component.html',
  styleUrl: './transactions-root.component.scss'
})
export class TransactionsRootComponent {

}
