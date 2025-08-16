import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface MenuItem {
  label: string;
  icon?: string;
  routerLink?: string | any[];
  command?: (event?: any) => void;
}

@Injectable({
  providedIn: 'root'
})
export class MenuService {
  private menuItemsSource = new BehaviorSubject<MenuItem[]>([]);
  currentMenuItems$ = this.menuItemsSource.asObservable();

  constructor() { }

  updateMenuItems(items: MenuItem[]): void {
    this.menuItemsSource.next(items);
  }
}
