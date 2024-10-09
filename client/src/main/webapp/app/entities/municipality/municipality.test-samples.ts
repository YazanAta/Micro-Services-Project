import { IMunicipality, NewMunicipality } from './municipality.model';

export const sampleWithRequiredData: IMunicipality = {
  id: 26922,
  name: 'freely plus',
};

export const sampleWithPartialData: IMunicipality = {
  id: 18122,
  name: 'crooked as culminate',
};

export const sampleWithFullData: IMunicipality = {
  id: 30364,
  name: 'aw bonnet',
};

export const sampleWithNewData: NewMunicipality = {
  name: 'about joyfully',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
