import { IBrigade, NewBrigade } from './brigade.model';

export const sampleWithRequiredData: IBrigade = {
  id: 962,
  name: 'oh grounded',
};

export const sampleWithPartialData: IBrigade = {
  id: 30005,
  name: 'uh-huh ew',
  type: 'political shameful until',
};

export const sampleWithFullData: IBrigade = {
  id: 8372,
  name: 'underneath',
  type: 'oof',
  establishedYear: 30962,
};

export const sampleWithNewData: NewBrigade = {
  name: 'overcoat besides the',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
