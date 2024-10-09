import { IGovernorate, NewGovernorate } from './governorate.model';

export const sampleWithRequiredData: IGovernorate = {
  id: 21217,
  name: 'unfortunately barring somber',
};

export const sampleWithPartialData: IGovernorate = {
  id: 7501,
  name: 'failing except footrest',
  population: 27886,
};

export const sampleWithFullData: IGovernorate = {
  id: 22559,
  name: 'following naturally quickly',
  area: 27688.32,
  population: 16884,
};

export const sampleWithNewData: NewGovernorate = {
  name: 'now below persecute',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
