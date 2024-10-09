export interface IGovernorate {
  id: number;
  name?: string | null;
  area?: number | null;
  population?: number | null;
}

export type NewGovernorate = Omit<IGovernorate, 'id'> & { id: null };
