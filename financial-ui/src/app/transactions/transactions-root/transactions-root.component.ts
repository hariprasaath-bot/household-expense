import { Component, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { RippleModule } from 'primeng/ripple';
import { MenuService } from '../../core/services/menu.service';
import { PrimeIcons } from 'primeng/api';

@Component({
  selector: 'app-transactions-root',
  standalone: true,
  imports: [RippleModule, RouterOutlet],
  templateUrl: './transactions-root.component.html',
  styleUrls: ['./transactions-root.component.scss']
})
export class TransactionsRootComponent implements OnInit {

  constructor(private router: Router, private menuService: MenuService) { }

  ngOnInit(): void {
    this.menuService.updateMenuItems([
      { label: 'Dashboard', routerLink: '/transactions/dashboard', icon: PrimeIcons.TH_LARGE },
      { label: 'manipulate', routerLink: '/transactions/manipulate', icon: PrimeIcons.UPLOAD },
      { label: 'Embedded', routerLink: '/transactions/embedded', icon: PrimeIcons.CHART_BAR },
      { label: 'summaries', routerLink: '/transactions/summaries', icon: PrimeIcons.LIST }
    ]);
  }

  moveToMenu(menu: String) {
    console.log("Navigating to menu: " + menu);
    this.router.navigate(["transactions/" + menu])
  }
}
