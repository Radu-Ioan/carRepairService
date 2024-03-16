import { ICarRepairAppointment } from 'app/shared/model/car-repair-appointment.model';
import { ICarServiceEmployee } from 'app/shared/model/car-service-employee.model';

export interface ICarService {
  id?: number;
  name?: string | null;
  address?: string;
  repairAppointments?: ICarRepairAppointment[] | null;
  employees?: ICarServiceEmployee[] | null;
}

export const defaultValue: Readonly<ICarService> = {};
