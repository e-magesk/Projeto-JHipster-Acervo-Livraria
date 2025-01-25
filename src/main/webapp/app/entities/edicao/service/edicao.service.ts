import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEdicao, NewEdicao } from '../edicao.model';

export type PartialUpdateEdicao = Partial<IEdicao> & Pick<IEdicao, 'id'>;

type RestOf<T extends IEdicao | NewEdicao> = Omit<T, 'dataLancamento'> & {
  dataLancamento?: string | null;
};

export type RestEdicao = RestOf<IEdicao>;

export type NewRestEdicao = RestOf<NewEdicao>;

export type PartialUpdateRestEdicao = RestOf<PartialUpdateEdicao>;

export type EntityResponseType = HttpResponse<IEdicao>;
export type EntityArrayResponseType = HttpResponse<IEdicao[]>;

@Injectable({ providedIn: 'root' })
export class EdicaoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/edicaos');

  create(edicao: NewEdicao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(edicao);
    return this.http
      .post<RestEdicao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(edicao: IEdicao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(edicao);
    return this.http
      .put<RestEdicao>(`${this.resourceUrl}/${this.getEdicaoIdentifier(edicao)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(edicao: PartialUpdateEdicao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(edicao);
    return this.http
      .patch<RestEdicao>(`${this.resourceUrl}/${this.getEdicaoIdentifier(edicao)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEdicao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEdicao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEdicaoIdentifier(edicao: Pick<IEdicao, 'id'>): number {
    return edicao.id;
  }

  compareEdicao(o1: Pick<IEdicao, 'id'> | null, o2: Pick<IEdicao, 'id'> | null): boolean {
    return o1 && o2 ? this.getEdicaoIdentifier(o1) === this.getEdicaoIdentifier(o2) : o1 === o2;
  }

  addEdicaoToCollectionIfMissing<Type extends Pick<IEdicao, 'id'>>(
    edicaoCollection: Type[],
    ...edicaosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const edicaos: Type[] = edicaosToCheck.filter(isPresent);
    if (edicaos.length > 0) {
      const edicaoCollectionIdentifiers = edicaoCollection.map(edicaoItem => this.getEdicaoIdentifier(edicaoItem));
      const edicaosToAdd = edicaos.filter(edicaoItem => {
        const edicaoIdentifier = this.getEdicaoIdentifier(edicaoItem);
        if (edicaoCollectionIdentifiers.includes(edicaoIdentifier)) {
          return false;
        }
        edicaoCollectionIdentifiers.push(edicaoIdentifier);
        return true;
      });
      return [...edicaosToAdd, ...edicaoCollection];
    }
    return edicaoCollection;
  }

  protected convertDateFromClient<T extends IEdicao | NewEdicao | PartialUpdateEdicao>(edicao: T): RestOf<T> {
    return {
      ...edicao,
      dataLancamento: edicao.dataLancamento?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEdicao: RestEdicao): IEdicao {
    return {
      ...restEdicao,
      dataLancamento: restEdicao.dataLancamento ? dayjs(restEdicao.dataLancamento) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEdicao>): HttpResponse<IEdicao> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEdicao[]>): HttpResponse<IEdicao[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
