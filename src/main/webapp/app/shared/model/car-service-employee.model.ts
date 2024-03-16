import { ICarService } from 'app/shared/model/car-service.model';
import { ICarRepairAppointment } from 'app/shared/model/car-repair-appointment.model';

export interface ICarServiceEmployee {
  id?: number;
  name?: string;
  age?: number | null;
  yearsOfExperience?: number | null;
  carService?: ICarService;
  repairAppointments?: ICarRepairAppointment[] | null;
}

export const defaultValue: Readonly<ICarServiceEmployee> = {};
