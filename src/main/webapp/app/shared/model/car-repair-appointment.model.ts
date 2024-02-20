import dayjs from 'dayjs';
import { ICar } from 'app/shared/model/car.model';
import { ICarService } from 'app/shared/model/car-service.model';

export interface ICarRepairAppointment {
  id?: number;
  date?: dayjs.Dayjs;
  car?: ICar;
  carService?: ICarService;
}

export const defaultValue: Readonly<ICarRepairAppointment> = {};
