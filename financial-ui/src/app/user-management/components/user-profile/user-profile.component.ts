import { Component, OnInit } from '@angular/core';

export interface BankAccount {
  bankName: string;
  accountType: string;
  last4: string;
}

export interface Category {
  id: string;
  name: string;
  isCustom: boolean;
}

export interface User {
  name: string;
  email: string | null;
  memberSince: Date | null;
  bankAccounts: BankAccount[];
  categories: Category[];
}

export interface Statistics {
  filesUploaded: number;
  totalTransactions: number;
  monthlySpending: number;
  topCategory: string;
}
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MenuService } from '../../../core/services/menu.service';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
  import { TooltipModule } from 'primeng/tooltip';
import { PrimeIcons } from 'primeng/api';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, FormsModule, InputTextModule, ButtonModule, TooltipModule],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  isEditMode = false;

  user: User = {
    name: 'New User',
    email: 'newuser@example.com',
    memberSince: new Date(),
    bankAccounts: [],
    categories: [
      { id: '1', name: 'FOOD', isCustom: false },
      { id: '2', name: 'TRANSPORT', isCustom: false },
      { id: '3', name: 'BILLS', isCustom: false },
      { id: '4', name: 'SHOPPING', isCustom: false },
      { id: '5', name: 'ENTERTAINMENT', isCustom: false },
      { id: '6', name: 'HEALTH', isCustom: false },
      { id: '7', name: 'TRAVEL', isCustom: false },
      { id: '8', name: 'INCOME', isCustom: false },
      { id: '9', name: 'OTHER', isCustom: false }
    ]
  };

  statistics: Statistics = {
    filesUploaded: 0,
    totalTransactions: 0,
    monthlySpending: 0,
    topCategory: ''
  };

  constructor(private menuService: MenuService) {}

  ngOnInit(): void {
    this.menuService.updateMenuItems([
      { label: 'Dashboard', routerLink: '/transactions/dashboard', icon: PrimeIcons.TH_LARGE },
      { label: 'manipulate', routerLink: '/transactions/manipulate', icon: PrimeIcons.UPLOAD },
      { label: 'Embedded', routerLink: '/transactions/embedded', icon: PrimeIcons.CHART_BAR },
      { label: 'summaries', routerLink: '/transactions/summaries', icon: PrimeIcons.LIST }
    ]);
  }

  saveProfile(): void {
    console.log('Profile saved:', this.user);
    this.isEditMode = false;

  }

  removeBankAccount(accountToRemove: BankAccount): void {
    this.user.bankAccounts = this.user.bankAccounts.filter(
      account => account !== accountToRemove
    );
  }

  changePassword(): void {
    console.log('Change password clicked');
  } 

  addBankAccount(bankName: string): void {
    if (bankName) {
      this.user.bankAccounts.push({ bankName, accountType: 'Savings', last4: '1234' });
    }
  }
  toggleEdit(): void {
    this.isEditMode = !this.isEditMode;
  }

  addCategory(categoryName: string): void {
    if (categoryName && !this.user.categories.some(c => c.name.toUpperCase() === categoryName.toUpperCase())) {
      const newCategory: Category = {
        id: Date.now().toString(),
        name: categoryName.toUpperCase(),
        isCustom: true
      };
      this.user.categories.push(newCategory);
    }
  }

  removeCategory(categoryToRemove: Category): void {
    if (categoryToRemove.isCustom) {
      this.user.categories = this.user.categories.filter(
        category => category !== categoryToRemove
      );
    }
  }

  getCategories(): string[] {
    return this.user.categories.map(cat => cat.name);
  }
}
