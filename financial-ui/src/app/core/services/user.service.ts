import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Category } from '../../transactions/components/transaction-upload/transaction-upload.types';

export interface User {
  id: string;
  userId: string;
  name: string;
  email: string | null;
  memberSince: Date | null;
  categories: Category[];
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userProfile = new BehaviorSubject<User | null>(null);
  userProfile$ = this.userProfile.asObservable();

  constructor() {
    // Initialize with default user data or load from API
    this.initializeUser();
  }

  private initializeUser(): void {
    // TODO: Replace this with actual API call to get user data
    const defaultUser: User = {
      id: '1',
      userId: 'demo-user-123',
      name: 'Demo User',
      email: 'demo@example.com',
      memberSince: new Date(),
      categories: [
        { 
          id: 'cat-1',
          name: 'Food',
          isCustom: false,
          categoryType: 'EXPENSE',
          searchString: 'food'
        },
        { 
          id: 'cat-2',
          name: 'Transportation',
          isCustom: false,
          categoryType: 'EXPENSE',
          searchString: 'transportation'
        },
        { 
          id: 'cat-3',
          name: 'Entertainment',
          isCustom: false,
          categoryType: 'EXPENSE',
          searchString: 'entertainment'
        },
        { 
          id: 'cat-4',
          name: 'Salary',
          isCustom: false,
          categoryType: 'INCOME',
          searchString: 'salary'
        }
      ]
    };
    
    this.userProfile.next(defaultUser);
  }

  // Add methods to update user data as needed
  updateUserProfile(updates: Partial<User>): void {
    const currentUser = this.userProfile.value;
    if (currentUser) {
      this.userProfile.next({ ...currentUser, ...updates });
    }
  }

  // Get current user data
  getCurrentUser(): User | null {
    return this.userProfile.value;
  }
}
