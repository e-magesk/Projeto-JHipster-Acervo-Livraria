import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompra, NewCompra } from '../compra.model';

export type PartialUpdateCompra = Partial<ICompra> & Pick<ICompra, 'id'>;

export type EntityResponseType = HttpResponse<ICompra>;
export type EntityArrayResponseType = HttpResponse<ICompra[]>;

@Injectable({ providedIn: 'root' })
export class CompraService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compras');

  create(compra: NewCompra): Observable<EntityResponseType> {
    return this.http.post<ICompra>(this.resourceUrl, compra, { observe: 'response' });
  }

  update(compra: ICompra): Observable<EntityResponseType> {
    return this.http.put<ICompra>(`${this.resourceUrl}/${this.getCompraIdentifier(compra)}`, compra, { observe: 'response' });
  }

  partialUpdate(compra: PartialUpdateCompra): Observable<EntityResponseType> {
    return this.http.patch<ICompra>(`${this.resourceUrl}/${this.getCompraIdentifier(compra)}`, compra, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompra[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCompraIdentifier(compra: Pick<ICompra, 'id'>): number {
    return compra.id;
  }

  compareCompra(o1: Pick<ICompra, 'id'> | null, o2: Pick<ICompra, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompraIdentifier(o1) === this.getCompraIdentifier(o2) : o1 === o2;
  }

  addCompraToCollectionIfMissing<Type extends Pick<ICompra, 'id'>>(
    compraCollection: Type[],
    ...comprasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const compras: Type[] = comprasToCheck.filter(isPresent);
    if (compras.length > 0) {
      const compraCollectionIdentifiers = compraCollection.map(compraItem => this.getCompraIdentifier(compraItem));
      const comprasToAdd = compras.filter(compraItem => {
        const compraIdentifier = this.getCompraIdentifier(compraItem);
        if (compraCollectionIdentifiers.includes(compraIdentifier)) {
          return false;
        }
        compraCollectionIdentifiers.push(compraIdentifier);
        return true;
      });
      return [...comprasToAdd, ...compraCollection];
    }
    return compraCollection;
  }

  getSumCompras(): Observable<HttpResponse<number>> {
    return this.http.get<number>(`${this.resourceUrl}/totalSum`, { observe: 'response' });
  }
}
