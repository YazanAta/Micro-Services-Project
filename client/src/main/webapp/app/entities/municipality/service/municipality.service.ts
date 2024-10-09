import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMunicipality, NewMunicipality } from '../municipality.model';

export type PartialUpdateMunicipality = Partial<IMunicipality> & Pick<IMunicipality, 'id'>;

export type EntityResponseType = HttpResponse<IMunicipality>;
export type EntityArrayResponseType = HttpResponse<IMunicipality[]>;

@Injectable({ providedIn: 'root' })
export class MunicipalityService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/municipalities', 'serviceone');

  create(municipality: NewMunicipality): Observable<EntityResponseType> {
    return this.http.post<IMunicipality>(this.resourceUrl, municipality, { observe: 'response' });
  }

  update(municipality: IMunicipality): Observable<EntityResponseType> {
    return this.http.put<IMunicipality>(`${this.resourceUrl}/${this.getMunicipalityIdentifier(municipality)}`, municipality, {
      observe: 'response',
    });
  }

  partialUpdate(municipality: PartialUpdateMunicipality): Observable<EntityResponseType> {
    return this.http.patch<IMunicipality>(`${this.resourceUrl}/${this.getMunicipalityIdentifier(municipality)}`, municipality, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMunicipality>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByBrigadeId(id: number): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}/brigade/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMunicipality[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMunicipalityIdentifier(municipality: Pick<IMunicipality, 'id'>): number {
    return municipality.id;
  }

  compareMunicipality(o1: Pick<IMunicipality, 'id'> | null, o2: Pick<IMunicipality, 'id'> | null): boolean {
    return o1 && o2 ? this.getMunicipalityIdentifier(o1) === this.getMunicipalityIdentifier(o2) : o1 === o2;
  }

  addMunicipalityToCollectionIfMissing<Type extends Pick<IMunicipality, 'id'>>(
    municipalityCollection: Type[],
    ...municipalitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const municipalities: Type[] = municipalitiesToCheck.filter(isPresent);
    if (municipalities.length > 0) {
      const municipalityCollectionIdentifiers = municipalityCollection.map(municipalityItem =>
        this.getMunicipalityIdentifier(municipalityItem),
      );
      const municipalitiesToAdd = municipalities.filter(municipalityItem => {
        const municipalityIdentifier = this.getMunicipalityIdentifier(municipalityItem);
        if (municipalityCollectionIdentifiers.includes(municipalityIdentifier)) {
          return false;
        }
        municipalityCollectionIdentifiers.push(municipalityIdentifier);
        return true;
      });
      return [...municipalitiesToAdd, ...municipalityCollection];
    }
    return municipalityCollection;
  }
}
