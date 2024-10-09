import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBrigade } from '../brigade.model';
import { BrigadeService } from '../service/brigade.service';

const brigadeResolve = (route: ActivatedRouteSnapshot): Observable<null | IBrigade> => {
  const id = route.params.id;
  if (id) {
    return inject(BrigadeService)
      .find(id)
      .pipe(
        mergeMap((brigade: HttpResponse<IBrigade>) => {
          if (brigade.body) {
            return of(brigade.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default brigadeResolve;
