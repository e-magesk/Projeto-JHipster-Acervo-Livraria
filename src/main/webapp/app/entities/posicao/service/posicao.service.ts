import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPosicao, NewPosicao } from '../posicao.model';

export type PartialUpdatePosicao = Partial<IPosicao> & Pick<IPosicao, 'id'>;

export type EntityResponseType = HttpResponse<IPosicao>;
export type EntityArrayResponseType = HttpResponse<IPosicao[]>;

@Injectable({ providedIn: 'root' })
export class PosicaoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/posicaos');

  create(posicao: NewPosicao): Observable<EntityResponseType> {
    return this.http.post<IPosicao>(this.resourceUrl, posicao, { observe: 'response' });
  }

  update(posicao: IPosicao): Observable<EntityResponseType> {
    return this.http.put<IPosicao>(`${this.resourceUrl}/${this.getPosicaoIdentifier(posicao)}`, posicao, { observe: 'response' });
  }

  partialUpdate(posicao: PartialUpdatePosicao): Observable<EntityResponseType> {
    return this.http.patch<IPosicao>(`${this.resourceUrl}/${this.getPosicaoIdentifier(posicao)}`, posicao, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPosicao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPosicao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPosicaoIdentifier(posicao: Pick<IPosicao, 'id'>): number {
    return posicao.id;
  }

  comparePosicao(o1: Pick<IPosicao, 'id'> | null, o2: Pick<IPosicao, 'id'> | null): boolean {
    return o1 && o2 ? this.getPosicaoIdentifier(o1) === this.getPosicaoIdentifier(o2) : o1 === o2;
  }

  addPosicaoToCollectionIfMissing<Type extends Pick<IPosicao, 'id'>>(
    posicaoCollection: Type[],
    ...posicaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const posicaos: Type[] = posicaosToCheck.filter(isPresent);
    if (posicaos.length > 0) {
      const posicaoCollectionIdentifiers = posicaoCollection.map(posicaoItem => this.getPosicaoIdentifier(posicaoItem));
      const posicaosToAdd = posicaos.filter(posicaoItem => {
        const posicaoIdentifier = this.getPosicaoIdentifier(posicaoItem);
        if (posicaoCollectionIdentifiers.includes(posicaoIdentifier)) {
          return false;
        }
        posicaoCollectionIdentifiers.push(posicaoIdentifier);
        return true;
      });
      return [...posicaosToAdd, ...posicaoCollection];
    }
    return posicaoCollection;
  }
}
