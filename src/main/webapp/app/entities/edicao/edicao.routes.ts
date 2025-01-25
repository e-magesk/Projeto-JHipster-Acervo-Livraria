import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EdicaoResolve from './route/edicao-routing-resolve.service';

const edicaoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/edicao.component').then(m => m.EdicaoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/edicao-detail.component').then(m => m.EdicaoDetailComponent),
    resolve: {
      edicao: EdicaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/edicao-update.component').then(m => m.EdicaoUpdateComponent),
    resolve: {
      edicao: EdicaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/edicao-update.component').then(m => m.EdicaoUpdateComponent),
    resolve: {
      edicao: EdicaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default edicaoRoute;
