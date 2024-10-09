import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BrigadeResolve from './route/brigade-routing-resolve.service';

const brigadeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/brigade.component').then(m => m.BrigadeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/brigade-detail.component').then(m => m.BrigadeDetailComponent),
    resolve: {
      brigade: BrigadeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/brigade-update.component').then(m => m.BrigadeUpdateComponent),
    resolve: {
      brigade: BrigadeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/brigade-update.component').then(m => m.BrigadeUpdateComponent),
    resolve: {
      brigade: BrigadeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default brigadeRoute;
