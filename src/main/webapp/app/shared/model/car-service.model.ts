export interface ICarService {
  id?: number;
  name?: string | null;
  address?: string;
}

export const defaultValue: Readonly<ICarService> = {};
