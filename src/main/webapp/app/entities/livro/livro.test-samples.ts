import { ILivro, NewLivro } from './livro.model';

export const sampleWithRequiredData: ILivro = {
  id: 158,
  titulo: 'calculating even',
};

export const sampleWithPartialData: ILivro = {
  id: 20420,
  titulo: 'aha excepting zowie',
  genero: 'ROMANCE',
};

export const sampleWithFullData: ILivro = {
  id: 2308,
  titulo: 'until woot geez',
  genero: 'BIOGRAFIA',
};

export const sampleWithNewData: NewLivro = {
  titulo: 'aha',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
