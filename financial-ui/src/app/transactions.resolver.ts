import { ResolveFn } from '@angular/router';

export const transactionsResolver: ResolveFn<boolean> = (route, state) => {
  return true;
};
