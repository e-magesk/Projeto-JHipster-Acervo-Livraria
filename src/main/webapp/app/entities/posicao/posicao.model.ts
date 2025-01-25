export interface IPosicao {
  id: number;
  codigo?: string | null;
}

export type NewPosicao = Omit<IPosicao, 'id'> & { id: null };
