import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CarServiceEmployee from './car-service-employee';
import CarServiceEmployeeDetail from './car-service-employee-detail';
import CarServiceEmployeeUpdate from './car-service-employee-update';
import CarServiceEmployeeDeleteDialog from './car-service-employee-delete-dialog';

const CarServiceEmployeeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CarServiceEmployee />} />
    <Route path="new" element={<CarServiceEmployeeUpdate />} />
    <Route path=":id">
      <Route index element={<CarServiceEmployeeDetail />} />
      <Route path="edit" element={<CarServiceEmployeeUpdate />} />
      <Route path="delete" element={<CarServiceEmployeeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CarServiceEmployeeRoutes;
