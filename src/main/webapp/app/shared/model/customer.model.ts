import { IWishList } from 'app/shared/model/wish-list.model';
import { IAddress } from 'app/shared/model/address.model';

export interface ICustomer {
  id?: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  telephone?: string | null;
  wishLists?: IWishList[] | null;
  addresses?: IAddress[] | null;
}

export const defaultValue: Readonly<ICustomer> = {};
