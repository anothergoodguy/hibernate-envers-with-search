import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Address from './address';
import AddressDetail from './address-detail';
import AddressUpdate from './address-update';
import AddressDeleteDialog from './address-delete-dialog';

const AddressRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Address />} />
    <Route path="new" element={<AddressUpdate />} />
    <Route path=":id">
      <Route index element={<AddressDetail />} />
      <Route path="edit" element={<AddressUpdate />} />
      <Route path="delete" element={<AddressDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AddressRoutes;
