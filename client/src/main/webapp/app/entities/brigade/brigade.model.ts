import { IGovernorate } from 'app/entities/governorate/governorate.model';

export interface IBrigade {
  id: number;
  name?: string | null;
  type?: string | null;
  establishedYear?: number | null;
  governorate?: Pick<IGovernorate, 'id'> | null;
}

export type NewBrigade = Omit<IBrigade, 'id'> & { id: null };
