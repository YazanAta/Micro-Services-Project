import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBrigade } from '../brigade.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../brigade.test-samples';

import { BrigadeService } from './brigade.service';

const requireRestSample: IBrigade = {
  ...sampleWithRequiredData,
};

describe('Brigade Service', () => {
  let service: BrigadeService;
  let httpMock: HttpTestingController;
  let expectedResult: IBrigade | IBrigade[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BrigadeService);
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

    it('should create a Brigade', () => {
      const brigade = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(brigade).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Brigade', () => {
      const brigade = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(brigade).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Brigade', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Brigade', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Brigade', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBrigadeToCollectionIfMissing', () => {
      it('should add a Brigade to an empty array', () => {
        const brigade: IBrigade = sampleWithRequiredData;
        expectedResult = service.addBrigadeToCollectionIfMissing([], brigade);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(brigade);
      });

      it('should not add a Brigade to an array that contains it', () => {
        const brigade: IBrigade = sampleWithRequiredData;
        const brigadeCollection: IBrigade[] = [
          {
            ...brigade,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBrigadeToCollectionIfMissing(brigadeCollection, brigade);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Brigade to an array that doesn't contain it", () => {
        const brigade: IBrigade = sampleWithRequiredData;
        const brigadeCollection: IBrigade[] = [sampleWithPartialData];
        expectedResult = service.addBrigadeToCollectionIfMissing(brigadeCollection, brigade);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(brigade);
      });

      it('should add only unique Brigade to an array', () => {
        const brigadeArray: IBrigade[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const brigadeCollection: IBrigade[] = [sampleWithRequiredData];
        expectedResult = service.addBrigadeToCollectionIfMissing(brigadeCollection, ...brigadeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const brigade: IBrigade = sampleWithRequiredData;
        const brigade2: IBrigade = sampleWithPartialData;
        expectedResult = service.addBrigadeToCollectionIfMissing([], brigade, brigade2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(brigade);
        expect(expectedResult).toContain(brigade2);
      });

      it('should accept null and undefined values', () => {
        const brigade: IBrigade = sampleWithRequiredData;
        expectedResult = service.addBrigadeToCollectionIfMissing([], null, brigade, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(brigade);
      });

      it('should return initial array if no Brigade is added', () => {
        const brigadeCollection: IBrigade[] = [sampleWithRequiredData];
        expectedResult = service.addBrigadeToCollectionIfMissing(brigadeCollection, undefined, null);
        expect(expectedResult).toEqual(brigadeCollection);
      });
    });

    describe('compareBrigade', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBrigade(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBrigade(entity1, entity2);
        const compareResult2 = service.compareBrigade(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBrigade(entity1, entity2);
        const compareResult2 = service.compareBrigade(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBrigade(entity1, entity2);
        const compareResult2 = service.compareBrigade(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
