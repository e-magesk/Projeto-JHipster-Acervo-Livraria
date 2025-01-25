import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CompraResolve from './route/compra-routing-resolve.service';

const compraRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/compra.component').then(m => m.CompraComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/compra-detail.component').then(m => m.CompraDetailComponent),
    resolve: {
      compra: CompraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/compra-update.component').then(m => m.CompraUpdateComponent),
    resolve: {
      compra: CompraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/compra-update.component').then(m => m.CompraUpdateComponent),
    resolve: {
      compra: CompraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default compraRoute;
