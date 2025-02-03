import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { RippleModule } from 'primeng/ripple';

@Component({
  selector: 'app-transactions-root',
  standalone: true,
  imports: [RippleModule, RouterOutlet],
  templateUrl: './transactions-root.component.html',
  styleUrl: './transactions-root.component.scss'
})
export class TransactionsRootComponent {

  public constructor(private router: Router){

  }

  moveToMenu(menu:String){
    this.router.navigate(["transactions/" + menu])
  }
}
