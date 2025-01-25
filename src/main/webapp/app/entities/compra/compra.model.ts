import { IEdicao } from 'app/entities/edicao/edicao.model';

export interface ICompra {
  id: number;
  quantidade?: number | null;
  precoCompra?: number | null;
  valorTotal?: number | null;
  edicao?: IEdicao | null;
}

export type NewCompra = Omit<ICompra, 'id'> & { id: null };
