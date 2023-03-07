import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Category from './category';
import Product from './product';
import Customer from './customer';
import Address from './address';
import WishList from './wish-list';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="category/*" element={<Category />} />
        <Route path="product/*" element={<Product />} />
        <Route path="customer/*" element={<Customer />} />
        <Route path="address/*" element={<Address />} />
        <Route path="wish-list/*" element={<WishList />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
