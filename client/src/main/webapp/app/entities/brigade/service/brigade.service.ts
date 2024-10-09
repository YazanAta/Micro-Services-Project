import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBrigade, NewBrigade } from '../brigade.model';

export type PartialUpdateBrigade = Partial<IBrigade> & Pick<IBrigade, 'id'>;

export type EntityResponseType = HttpResponse<IBrigade>;
export type EntityArrayResponseType = HttpResponse<IBrigade[]>;

@Injectable({ providedIn: 'root' })
export class BrigadeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/brigades', 'serviceone');

  create(brigade: NewBrigade): Observable<EntityResponseType> {
    return this.http.post<IBrigade>(this.resourceUrl, brigade, { observe: 'response' });
  }

  update(brigade: IBrigade): Observable<EntityResponseType> {
    return this.http.put<IBrigade>(`${this.resourceUrl}/${this.getBrigadeIdentifier(brigade)}`, brigade, { observe: 'response' });
  }

  partialUpdate(brigade: PartialUpdateBrigade): Observable<EntityResponseType> {
    return this.http.patch<IBrigade>(`${this.resourceUrl}/${this.getBrigadeIdentifier(brigade)}`, brigade, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBrigade>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByGovernorateId(id: number): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/governorate/${id}`, { observe: 'response' });
  }
  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBrigade[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBrigadeIdentifier(brigade: Pick<IBrigade, 'id'>): number {
    return brigade.id;
  }

  compareBrigade(o1: Pick<IBrigade, 'id'> | null, o2: Pick<IBrigade, 'id'> | null): boolean {
    return o1 && o2 ? this.getBrigadeIdentifier(o1) === this.getBrigadeIdentifier(o2) : o1 === o2;
  }

  addBrigadeToCollectionIfMissing<Type extends Pick<IBrigade, 'id'>>(
    brigadeCollection: Type[],
    ...brigadesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const brigades: Type[] = brigadesToCheck.filter(isPresent);
    if (brigades.length > 0) {
      const brigadeCollectionIdentifiers = brigadeCollection.map(brigadeItem => this.getBrigadeIdentifier(brigadeItem));
      const brigadesToAdd = brigades.filter(brigadeItem => {
        const brigadeIdentifier = this.getBrigadeIdentifier(brigadeItem);
        if (brigadeCollectionIdentifiers.includes(brigadeIdentifier)) {
          return false;
        }
        brigadeCollectionIdentifiers.push(brigadeIdentifier);
        return true;
      });
      return [...brigadesToAdd, ...brigadeCollection];
    }
    return brigadeCollection;
  }
}
