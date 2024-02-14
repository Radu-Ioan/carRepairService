export interface ICarServiceEmployee {
  id?: number;
  name?: string;
  age?: number | null;
  yearsOfExperience?: number | null;
}

export const defaultValue: Readonly<ICarServiceEmployee> = {};
