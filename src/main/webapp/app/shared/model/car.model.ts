export interface ICar {
  id?: number;
  company?: string | null;
  manufacturedYear?: number | null;
  ownerName?: string;
}

export const defaultValue: Readonly<ICar> = {};
