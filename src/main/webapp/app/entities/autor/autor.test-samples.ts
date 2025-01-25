import { IAutor, NewAutor } from './autor.model';

export const sampleWithRequiredData: IAutor = {
  id: 30808,
};

export const sampleWithPartialData: IAutor = {
  id: 31400,
};

export const sampleWithFullData: IAutor = {
  id: 8309,
  nome: 'ugh though',
  nacionalidade: 'INGLATERRA',
};

export const sampleWithNewData: NewAutor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
