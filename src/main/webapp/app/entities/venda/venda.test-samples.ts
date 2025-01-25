import { IVenda, NewVenda } from './venda.model';

export const sampleWithRequiredData: IVenda = {
  id: 21309,
  quantidade: 17811,
  precoVenda: 17420.41,
};

export const sampleWithPartialData: IVenda = {
  id: 20730,
  quantidade: 29607,
  precoVenda: 3166.95,
};

export const sampleWithFullData: IVenda = {
  id: 32471,
  quantidade: 19442,
  precoVenda: 12970.47,
  valorTotal: 2742.74,
};

export const sampleWithNewData: NewVenda = {
  quantidade: 11389,
  precoVenda: 30025.52,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
