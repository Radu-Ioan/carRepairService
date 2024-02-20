import { ICarRepairAppointment } from 'app/shared/model/car-repair-appointment.model';

export interface ICarService {
  id?: number;
  name?: string | null;
  address?: string;
  repairAppointments?: ICarRepairAppointment[] | null;
}

export const defaultValue: Readonly<ICarService> = {};
