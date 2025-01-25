import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEdicao } from '../edicao.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../edicao.test-samples';

import { EdicaoService, RestEdicao } from './edicao.service';

const requireRestSample: RestEdicao = {
  ...sampleWithRequiredData,
  dataLancamento: sampleWithRequiredData.dataLancamento?.format(DATE_FORMAT),
};

describe('Edicao Service', () => {
  let service: EdicaoService;
  let httpMock: HttpTestingController;
  let expectedResult: IEdicao | IEdicao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EdicaoService);
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

    it('should create a Edicao', () => {
      const edicao = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(edicao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Edicao', () => {
      const edicao = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(edicao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Edicao', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Edicao', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Edicao', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEdicaoToCollectionIfMissing', () => {
      it('should add a Edicao to an empty array', () => {
        const edicao: IEdicao = sampleWithRequiredData;
        expectedResult = service.addEdicaoToCollectionIfMissing([], edicao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(edicao);
      });

      it('should not add a Edicao to an array that contains it', () => {
        const edicao: IEdicao = sampleWithRequiredData;
        const edicaoCollection: IEdicao[] = [
          {
            ...edicao,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEdicaoToCollectionIfMissing(edicaoCollection, edicao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Edicao to an array that doesn't contain it", () => {
        const edicao: IEdicao = sampleWithRequiredData;
        const edicaoCollection: IEdicao[] = [sampleWithPartialData];
        expectedResult = service.addEdicaoToCollectionIfMissing(edicaoCollection, edicao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(edicao);
      });

      it('should add only unique Edicao to an array', () => {
        const edicaoArray: IEdicao[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const edicaoCollection: IEdicao[] = [sampleWithRequiredData];
        expectedResult = service.addEdicaoToCollectionIfMissing(edicaoCollection, ...edicaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const edicao: IEdicao = sampleWithRequiredData;
        const edicao2: IEdicao = sampleWithPartialData;
        expectedResult = service.addEdicaoToCollectionIfMissing([], edicao, edicao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(edicao);
        expect(expectedResult).toContain(edicao2);
      });

      it('should accept null and undefined values', () => {
        const edicao: IEdicao = sampleWithRequiredData;
        expectedResult = service.addEdicaoToCollectionIfMissing([], null, edicao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(edicao);
      });

      it('should return initial array if no Edicao is added', () => {
        const edicaoCollection: IEdicao[] = [sampleWithRequiredData];
        expectedResult = service.addEdicaoToCollectionIfMissing(edicaoCollection, undefined, null);
        expect(expectedResult).toEqual(edicaoCollection);
      });
    });

    describe('compareEdicao', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEdicao(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 32276 };
        const entity2 = null;

        const compareResult1 = service.compareEdicao(entity1, entity2);
        const compareResult2 = service.compareEdicao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 32276 };
        const entity2 = { id: 23410 };

        const compareResult1 = service.compareEdicao(entity1, entity2);
        const compareResult2 = service.compareEdicao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 32276 };
        const entity2 = { id: 32276 };

        const compareResult1 = service.compareEdicao(entity1, entity2);
        const compareResult2 = service.compareEdicao(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
