import dayjs from 'dayjs';
import { IProduct } from 'app/shared/model/product.model';
import { CategoryStatus } from 'app/shared/model/enumerations/category-status.model';

export interface ICategory {
  id?: string;
  description?: string;
  sortOrder?: number | null;
  dateAdded?: string | null;
  dateModified?: string | null;
  status?: CategoryStatus | null;
  parent?: ICategory | null;
  products?: IProduct[] | null;
}

export const defaultValue: Readonly<ICategory> = {};
