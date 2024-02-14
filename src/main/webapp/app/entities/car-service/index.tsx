import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CarService from './car-service';
import CarServiceDetail from './car-service-detail';
import CarServiceUpdate from './car-service-update';
import CarServiceDeleteDialog from './car-service-delete-dialog';

const CarServiceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CarService />} />
    <Route path="new" element={<CarServiceUpdate />} />
    <Route path=":id">
      <Route index element={<CarServiceDetail />} />
      <Route path="edit" element={<CarServiceUpdate />} />
      <Route path="delete" element={<CarServiceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CarServiceRoutes;
