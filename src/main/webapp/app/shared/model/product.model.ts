import dayjs from 'dayjs';
import { IWishList } from 'app/shared/model/wish-list.model';
import { ICategory } from 'app/shared/model/category.model';

export interface IProduct {
  id?: string;
  title?: string;
  keywords?: string | null;
  description?: string | null;
  rating?: number | null;
  dateAdded?: string | null;
  dateModified?: string | null;
  wishList?: IWishList | null;
  categories?: ICategory[] | null;
}

export const defaultValue: Readonly<IProduct> = {};
