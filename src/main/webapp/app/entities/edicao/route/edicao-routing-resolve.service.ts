import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEdicao } from '../edicao.model';
import { EdicaoService } from '../service/edicao.service';

const edicaoResolve = (route: ActivatedRouteSnapshot): Observable<null | IEdicao> => {
  const id = route.params.id;
  if (id) {
    return inject(EdicaoService)
      .find(id)
      .pipe(
        mergeMap((edicao: HttpResponse<IEdicao>) => {
          if (edicao.body) {
            return of(edicao.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default edicaoResolve;
