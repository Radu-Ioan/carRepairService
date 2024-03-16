import dayjs from 'dayjs';
import { ICar } from 'app/shared/model/car.model';
import { ICarService } from 'app/shared/model/car-service.model';
import { ICarServiceEmployee } from 'app/shared/model/car-service-employee.model';

export interface ICarRepairAppointment {
  id?: number;
  date?: dayjs.Dayjs;
  car?: ICar;
  carService?: ICarService;
  responsibleEmployees?: ICarServiceEmployee[] | null;
}

export const defaultValue: Readonly<ICarRepairAppointment> = {};
