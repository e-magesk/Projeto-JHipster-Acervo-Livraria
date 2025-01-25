import { Genero } from 'app/entities/enumerations/genero.model';

export interface ILivro {
  id: number;
  titulo?: string | null;
  genero?: keyof typeof Genero | null;
}

export type NewLivro = Omit<ILivro, 'id'> & { id: null };
