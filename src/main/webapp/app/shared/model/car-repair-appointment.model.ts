import dayjs from 'dayjs';
import { ICar } from 'app/shared/model/car.model';

export interface ICarRepairAppointment {
  id?: number;
  date?: dayjs.Dayjs;
  car?: ICar;
}

export const defaultValue: Readonly<ICarRepairAppointment> = {};
