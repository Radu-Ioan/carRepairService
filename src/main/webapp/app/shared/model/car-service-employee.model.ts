import { ICarService } from 'app/shared/model/car-service.model';

export interface ICarServiceEmployee {
  id?: number;
  name?: string;
  age?: number | null;
  yearsOfExperience?: number | null;
  carService?: ICarService;
}

export const defaultValue: Readonly<ICarServiceEmployee> = {};
