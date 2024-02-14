import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CarRepairAppointment from './car-repair-appointment';
import CarRepairAppointmentDetail from './car-repair-appointment-detail';
import CarRepairAppointmentUpdate from './car-repair-appointment-update';
import CarRepairAppointmentDeleteDialog from './car-repair-appointment-delete-dialog';

const CarRepairAppointmentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CarRepairAppointment />} />
    <Route path="new" element={<CarRepairAppointmentUpdate />} />
    <Route path=":id">
      <Route index element={<CarRepairAppointmentDetail />} />
      <Route path="edit" element={<CarRepairAppointmentUpdate />} />
      <Route path="delete" element={<CarRepairAppointmentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CarRepairAppointmentRoutes;
