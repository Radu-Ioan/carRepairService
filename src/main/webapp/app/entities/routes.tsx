import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Car from './car';
import CarService from './car-service';
import CarServiceEmployee from './car-service-employee';
import CarRepairAppointment from './car-repair-appointment';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="car/*" element={<Car />} />
        <Route path="car-service/*" element={<CarService />} />
        <Route path="car-service-employee/*" element={<CarServiceEmployee />} />
        <Route path="car-repair-appointment/*" element={<CarRepairAppointment />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
