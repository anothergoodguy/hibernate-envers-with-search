import { ICustomer } from 'app/shared/model/customer.model';

export interface IAddress {
  id?: string;
  address1?: string | null;
  address2?: string | null;
  city?: string | null;
  postcode?: string;
  country?: string;
  customer?: ICustomer | null;
}

export const defaultValue: Readonly<IAddress> = {};
