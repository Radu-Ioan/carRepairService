import dayjs from 'dayjs';

export interface ICarRepairAppointment {
  id?: number;
  date?: dayjs.Dayjs;
}

export const defaultValue: Readonly<ICarRepairAppointment> = {};
