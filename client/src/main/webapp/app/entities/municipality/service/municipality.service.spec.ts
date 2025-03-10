import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMunicipality } from '../municipality.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../municipality.test-samples';

import { MunicipalityService } from './municipality.service';

const requireRestSample: IMunicipality = {
  ...sampleWithRequiredData,
};

describe('Municipality Service', () => {
  let service: MunicipalityService;
  let httpMock: HttpTestingController;
  let expectedResult: IMunicipality | IMunicipality[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MunicipalityService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Municipality', () => {
      const municipality = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(municipality).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Municipality', () => {
      const municipality = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(municipality).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Municipality', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Municipality', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Municipality', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMunicipalityToCollectionIfMissing', () => {
      it('should add a Municipality to an empty array', () => {
        const municipality: IMunicipality = sampleWithRequiredData;
        expectedResult = service.addMunicipalityToCollectionIfMissing([], municipality);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(municipality);
      });

      it('should not add a Municipality to an array that contains it', () => {
        const municipality: IMunicipality = sampleWithRequiredData;
        const municipalityCollection: IMunicipality[] = [
          {
            ...municipality,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMunicipalityToCollectionIfMissing(municipalityCollection, municipality);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Municipality to an array that doesn't contain it", () => {
        const municipality: IMunicipality = sampleWithRequiredData;
        const municipalityCollection: IMunicipality[] = [sampleWithPartialData];
        expectedResult = service.addMunicipalityToCollectionIfMissing(municipalityCollection, municipality);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(municipality);
      });

      it('should add only unique Municipality to an array', () => {
        const municipalityArray: IMunicipality[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const municipalityCollection: IMunicipality[] = [sampleWithRequiredData];
        expectedResult = service.addMunicipalityToCollectionIfMissing(municipalityCollection, ...municipalityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const municipality: IMunicipality = sampleWithRequiredData;
        const municipality2: IMunicipality = sampleWithPartialData;
        expectedResult = service.addMunicipalityToCollectionIfMissing([], municipality, municipality2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(municipality);
        expect(expectedResult).toContain(municipality2);
      });

      it('should accept null and undefined values', () => {
        const municipality: IMunicipality = sampleWithRequiredData;
        expectedResult = service.addMunicipalityToCollectionIfMissing([], null, municipality, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(municipality);
      });

      it('should return initial array if no Municipality is added', () => {
        const municipalityCollection: IMunicipality[] = [sampleWithRequiredData];
        expectedResult = service.addMunicipalityToCollectionIfMissing(municipalityCollection, undefined, null);
        expect(expectedResult).toEqual(municipalityCollection);
      });
    });

    describe('compareMunicipality', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMunicipality(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMunicipality(entity1, entity2);
        const compareResult2 = service.compareMunicipality(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMunicipality(entity1, entity2);
        const compareResult2 = service.compareMunicipality(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMunicipality(entity1, entity2);
        const compareResult2 = service.compareMunicipality(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
