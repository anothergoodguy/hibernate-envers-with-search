import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Category from './category';
import CategoryDetail from './category-detail';
import CategoryUpdate from './category-update';
import CategoryDeleteDialog from './category-delete-dialog';

const CategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Category />} />
    <Route path="new" element={<CategoryUpdate />} />
    <Route path=":id">
      <Route index element={<CategoryDetail />} />
      <Route path="edit" element={<CategoryUpdate />} />
      <Route path="delete" element={<CategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CategoryRoutes;
