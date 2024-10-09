import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMunicipality } from '../municipality.model';
import { MunicipalityService } from '../service/municipality.service';

const municipalityResolve = (route: ActivatedRouteSnapshot): Observable<null | IMunicipality> => {
  const id = route.params.id;
  if (id) {
    return inject(MunicipalityService)
      .find(id)
      .pipe(
        mergeMap((municipality: HttpResponse<IMunicipality>) => {
          if (municipality.body) {
            return of(municipality.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default municipalityResolve;
