import { ICompra, NewCompra } from './compra.model';

export const sampleWithRequiredData: ICompra = {
  id: 5170,
  quantidade: 11448,
  precoCompra: 24657.08,
};

export const sampleWithPartialData: ICompra = {
  id: 32142,
  quantidade: 16043,
  precoCompra: 9019.74,
  valorTotal: 5345.28,
};

export const sampleWithFullData: ICompra = {
  id: 18569,
  quantidade: 6936,
  precoCompra: 30716.73,
  valorTotal: 3624.78,
};

export const sampleWithNewData: NewCompra = {
  quantidade: 29261,
  precoCompra: 5954.93,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
