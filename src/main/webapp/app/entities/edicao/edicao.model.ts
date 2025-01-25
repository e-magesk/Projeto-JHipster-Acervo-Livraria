import dayjs from 'dayjs/esm';
import { IPosicao } from 'app/entities/posicao/posicao.model';
import { ILivro } from 'app/entities/livro/livro.model';

export interface IEdicao {
  id: number;
  editora?: string | null;
  dataLancamento?: dayjs.Dayjs | null;
  quantidadeExemplares?: number | null;
  preco?: number | null;
  posicao?: IPosicao | null;
  livro?: ILivro | null;
}

export type NewEdicao = Omit<IEdicao, 'id'> & { id: null };
