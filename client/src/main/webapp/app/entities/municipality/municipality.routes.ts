import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MunicipalityResolve from './route/municipality-routing-resolve.service';

const municipalityRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/municipality.component').then(m => m.MunicipalityComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/municipality-detail.component').then(m => m.MunicipalityDetailComponent),
    resolve: {
      municipality: MunicipalityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/municipality-update.component').then(m => m.MunicipalityUpdateComponent),
    resolve: {
      municipality: MunicipalityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/municipality-update.component').then(m => m.MunicipalityUpdateComponent),
    resolve: {
      municipality: MunicipalityResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default municipalityRoute;
