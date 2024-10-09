import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 11370,
  login: "Q4aq`@90\\C9ub7Cu\\+YS5\\tum\\'t",
};

export const sampleWithPartialData: IUser = {
  id: 10655,
  login: 'Z7oe',
};

export const sampleWithFullData: IUser = {
  id: 10050,
  login: '~Xs2ka@UwvoI\\TsuzLWD\\nHRq2\\kvru\\ U',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
