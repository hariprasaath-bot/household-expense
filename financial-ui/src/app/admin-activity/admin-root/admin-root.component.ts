import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { RippleModule } from 'primeng/ripple';

@Component({
  selector: 'app-admin-root',
  standalone: true,
  imports: [RippleModule, RouterOutlet],
  templateUrl: './admin-root.component.html',
  styleUrl: './admin-root.component.scss'
})
export class AdminRootComponent {

    public constructor(private router: Router){
  
    }
  
    moveToMenu(menu:String){
      console.log("Navigating to menu: " + menu);
      this.router.navigate(["admin/" + menu])
    }

}
