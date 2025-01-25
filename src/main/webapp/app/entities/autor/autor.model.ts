import { ILivro } from 'app/entities/livro/livro.model';
import { Nacionalidade } from 'app/entities/enumerations/nacionalidade.model';

export interface IAutor {
  id: number;
  nome?: string | null;
  nacionalidade?: keyof typeof Nacionalidade | null;
  livros?: ILivro[] | null;
}

export type NewAutor = Omit<IAutor, 'id'> & { id: null };
