import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompra } from '../compra.model';
import { CompraService } from '../service/compra.service';

const compraResolve = (route: ActivatedRouteSnapshot): Observable<null | ICompra> => {
  const id = route.params.id;
  if (id) {
    return inject(CompraService)
      .find(id)
      .pipe(
        mergeMap((compra: HttpResponse<ICompra>) => {
          if (compra.body) {
            return of(compra.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default compraResolve;
