import { IBrigade } from 'app/entities/brigade/brigade.model';

export interface IMunicipality {
  id: number;
  name?: string | null;
  brigade?: Pick<IBrigade, 'id'> | null;
}

export type NewMunicipality = Omit<IMunicipality, 'id'> & { id: null };
