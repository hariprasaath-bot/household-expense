import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Observable } from 'rxjs';
import { MenuItem, MenuService } from '../../services/menu.service';
import { AuthService } from '../../../auth/service/auth.service';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.theme.scss']
})
export class MenuComponent implements OnInit {
  menuItems$!: Observable<MenuItem[]>;

  constructor(private menuService: MenuService , private authService: AuthService) {}

  ngOnInit(): void {
    this.menuItems$ = this.menuService.currentMenuItems$;
  }
  logout() {
    console.log("Logout clicked");
    this.authService.logout();
  }
}
