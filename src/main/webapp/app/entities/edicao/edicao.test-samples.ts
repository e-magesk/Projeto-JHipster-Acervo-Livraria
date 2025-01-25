import dayjs from 'dayjs/esm';

import { IEdicao, NewEdicao } from './edicao.model';

export const sampleWithRequiredData: IEdicao = {
  id: 5318,
  editora: 'furthermore',
  dataLancamento: dayjs('2025-01-24'),
  quantidadeExemplares: 14607,
  preco: 28696.41,
};

export const sampleWithPartialData: IEdicao = {
  id: 17706,
  editora: 'psst too geez',
  dataLancamento: dayjs('2025-01-24'),
  quantidadeExemplares: 30115,
  preco: 18265.75,
};

export const sampleWithFullData: IEdicao = {
  id: 22040,
  editora: 'concrete meh',
  dataLancamento: dayjs('2025-01-25'),
  quantidadeExemplares: 27862,
  preco: 3525.67,
};

export const sampleWithNewData: NewEdicao = {
  editora: 'piglet whisper not',
  dataLancamento: dayjs('2025-01-24'),
  quantidadeExemplares: 17447,
  preco: 384.73,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
