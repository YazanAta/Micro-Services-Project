import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGovernorate, NewGovernorate } from '../governorate.model';

export type PartialUpdateGovernorate = Partial<IGovernorate> & Pick<IGovernorate, 'id'>;

export type EntityResponseType = HttpResponse<IGovernorate>;
export type EntityArrayResponseType = HttpResponse<IGovernorate[]>;

@Injectable({ providedIn: 'root' })
export class GovernorateService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/governorates', 'serviceone');

  create(governorate: NewGovernorate): Observable<EntityResponseType> {
    return this.http.post<IGovernorate>(this.resourceUrl, governorate, { observe: 'response' });
  }

  update(governorate: IGovernorate): Observable<EntityResponseType> {
    return this.http.put<IGovernorate>(`${this.resourceUrl}/${this.getGovernorateIdentifier(governorate)}`, governorate, {
      observe: 'response',
    });
  }

  partialUpdate(governorate: PartialUpdateGovernorate): Observable<EntityResponseType> {
    return this.http.patch<IGovernorate>(`${this.resourceUrl}/${this.getGovernorateIdentifier(governorate)}`, governorate, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGovernorate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGovernorate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGovernorateIdentifier(governorate: Pick<IGovernorate, 'id'>): number {
    return governorate.id;
  }

  compareGovernorate(o1: Pick<IGovernorate, 'id'> | null, o2: Pick<IGovernorate, 'id'> | null): boolean {
    return o1 && o2 ? this.getGovernorateIdentifier(o1) === this.getGovernorateIdentifier(o2) : o1 === o2;
  }

  addGovernorateToCollectionIfMissing<Type extends Pick<IGovernorate, 'id'>>(
    governorateCollection: Type[],
    ...governoratesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const governorates: Type[] = governoratesToCheck.filter(isPresent);
    if (governorates.length > 0) {
      const governorateCollectionIdentifiers = governorateCollection.map(governorateItem => this.getGovernorateIdentifier(governorateItem));
      const governoratesToAdd = governorates.filter(governorateItem => {
        const governorateIdentifier = this.getGovernorateIdentifier(governorateItem);
        if (governorateCollectionIdentifiers.includes(governorateIdentifier)) {
          return false;
        }
        governorateCollectionIdentifiers.push(governorateIdentifier);
        return true;
      });
      return [...governoratesToAdd, ...governorateCollection];
    }
    return governorateCollection;
  }
}
