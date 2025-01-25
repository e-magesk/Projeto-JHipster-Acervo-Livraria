import { IPosicao, NewPosicao } from './posicao.model';

export const sampleWithRequiredData: IPosicao = {
  id: 27892,
  codigo: 'Q4-6540-j72417',
};

export const sampleWithPartialData: IPosicao = {
  id: 8987,
  codigo: 'J7-90-j0822',
};

export const sampleWithFullData: IPosicao = {
  id: 10872,
  codigo: 'N7-658-k5992',
};

export const sampleWithNewData: NewPosicao = {
  codigo: 'P0-224825-m49',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
