import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'clientApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'brigade',
    data: { pageTitle: 'clientApp.brigade.home.title' },
    loadChildren: () => import('./brigade/brigade.routes'),
  },
  {
    path: 'governorate',
    data: { pageTitle: 'clientApp.governorate.home.title' },
    loadChildren: () => import('./governorate/governorate.routes'),
  },
  {
    path: 'municipality',
    data: { pageTitle: 'clientApp.municipality.home.title' },
    loadChildren: () => import('./municipality/municipality.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
