export type DateRange = [Date, Date] | null;
export type DateFilterType = 'week' | 'month' | 'threeMonths' | 'custom' | null;

export interface SSEData {
  status: 'COMPLETED' | 'IN_PROGRESS' | 'ERROR';
  progress?: number;
  message?: string;
}

export interface Category {
  id: string;
  name: string;
  isCustom: boolean;
  categoryType: 'EXPENSE' | 'INCOME' | 'INVESTMENT';
  searchString?: string;
  userId?: string;
}

export interface CategoryOption {
  label: string;
  value: string;
  isCustom: boolean;
}

export interface User {
  id: string;
  userId: string;
  categories: Category[];
  username?: string;
  email?: string;
}

export interface UploadEvent {
  originalEvent: any;
  files: File[];
}

export interface Transaction {
  transactionId: string;
  aiGeneratedCategory: string;
  aiGeneratedDescription: string | null;
  aiGeneratedTransactionParty: string;
  balance: string;
  category: string;
  deposit: number;
  isUpiTransaction: boolean;
  recordDate: { $date: string };
  remark: string;
  transactionType: string;
  transaction_date: { $date: string };
  withdraw: number;
  userId: string;
}

export interface BatchUpdateModel {
  category: string | null;
  remark: string;
}
