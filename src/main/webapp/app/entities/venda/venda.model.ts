import { IEdicao } from 'app/entities/edicao/edicao.model';

export interface IVenda {
  id: number;
  quantidade?: number | null;
  precoVenda?: number | null;
  valorTotal?: number | null;
  edicao?: IEdicao | null;
}

export type NewVenda = Omit<IVenda, 'id'> & { id: null };
