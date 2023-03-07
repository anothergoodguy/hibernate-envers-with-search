import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import WishList from './wish-list';
import WishListDetail from './wish-list-detail';
import WishListUpdate from './wish-list-update';
import WishListDeleteDialog from './wish-list-delete-dialog';

const WishListRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<WishList />} />
    <Route path="new" element={<WishListUpdate />} />
    <Route path=":id">
      <Route index element={<WishListDetail />} />
      <Route path="edit" element={<WishListUpdate />} />
      <Route path="delete" element={<WishListDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default WishListRoutes;
