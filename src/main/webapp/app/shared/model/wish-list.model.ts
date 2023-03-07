import { IProduct } from 'app/shared/model/product.model';
import { ICustomer } from 'app/shared/model/customer.model';

export interface IWishList {
  id?: string;
  title?: string;
  restricted?: boolean | null;
  products?: IProduct[] | null;
  customer?: ICustomer | null;
}

export const defaultValue: Readonly<IWishList> = {
  restricted: false,
};
