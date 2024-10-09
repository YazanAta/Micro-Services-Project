import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '94a14a2f-a774-4813-bc97-ad8c2ae82a07',
};

export const sampleWithPartialData: IAuthority = {
  name: 'b9533670-459d-42cf-bf3a-88c4a151d83a',
};

export const sampleWithFullData: IAuthority = {
  name: '4a55020b-2b11-4fba-8b06-f45b254a4a5f',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
