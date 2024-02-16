import { ICarRepairAppointment } from 'app/shared/model/car-repair-appointment.model';

export interface ICar {
  id?: number;
  company?: string | null;
  manufacturedYear?: number | null;
  ownerName?: string;
  carRepairAppointment?: ICarRepairAppointment | null;
}

export const defaultValue: Readonly<ICar> = {};
