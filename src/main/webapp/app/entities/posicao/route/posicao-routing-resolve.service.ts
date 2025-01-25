import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPosicao } from '../posicao.model';
import { PosicaoService } from '../service/posicao.service';

const posicaoResolve = (route: ActivatedRouteSnapshot): Observable<null | IPosicao> => {
  const id = route.params.id;
  if (id) {
    return inject(PosicaoService)
      .find(id)
      .pipe(
        mergeMap((posicao: HttpResponse<IPosicao>) => {
          if (posicao.body) {
            return of(posicao.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default posicaoResolve;
