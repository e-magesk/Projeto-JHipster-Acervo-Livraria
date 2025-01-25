import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PosicaoResolve from './route/posicao-routing-resolve.service';

const posicaoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/posicao.component').then(m => m.PosicaoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/posicao-detail.component').then(m => m.PosicaoDetailComponent),
    resolve: {
      posicao: PosicaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/posicao-update.component').then(m => m.PosicaoUpdateComponent),
    resolve: {
      posicao: PosicaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/posicao-update.component').then(m => m.PosicaoUpdateComponent),
    resolve: {
      posicao: PosicaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default posicaoRoute;
