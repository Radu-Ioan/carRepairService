import car from 'app/entities/car/car.reducer';
import carService from 'app/entities/car-service/car-service.reducer';
import carServiceEmployee from 'app/entities/car-service-employee/car-service-employee.reducer';
import carRepairAppointment from 'app/entities/car-repair-appointment/car-repair-appointment.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  car,
  carService,
  carServiceEmployee,
  carRepairAppointment,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
