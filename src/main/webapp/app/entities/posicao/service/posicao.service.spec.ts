import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPosicao } from '../posicao.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../posicao.test-samples';

import { PosicaoService } from './posicao.service';

const requireRestSample: IPosicao = {
  ...sampleWithRequiredData,
};

describe('Posicao Service', () => {
  let service: PosicaoService;
  let httpMock: HttpTestingController;
  let expectedResult: IPosicao | IPosicao[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PosicaoService);
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

    it('should create a Posicao', () => {
      const posicao = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(posicao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Posicao', () => {
      const posicao = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(posicao).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Posicao', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Posicao', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Posicao', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPosicaoToCollectionIfMissing', () => {
      it('should add a Posicao to an empty array', () => {
        const posicao: IPosicao = sampleWithRequiredData;
        expectedResult = service.addPosicaoToCollectionIfMissing([], posicao);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(posicao);
      });

      it('should not add a Posicao to an array that contains it', () => {
        const posicao: IPosicao = sampleWithRequiredData;
        const posicaoCollection: IPosicao[] = [
          {
            ...posicao,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPosicaoToCollectionIfMissing(posicaoCollection, posicao);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Posicao to an array that doesn't contain it", () => {
        const posicao: IPosicao = sampleWithRequiredData;
        const posicaoCollection: IPosicao[] = [sampleWithPartialData];
        expectedResult = service.addPosicaoToCollectionIfMissing(posicaoCollection, posicao);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(posicao);
      });

      it('should add only unique Posicao to an array', () => {
        const posicaoArray: IPosicao[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const posicaoCollection: IPosicao[] = [sampleWithRequiredData];
        expectedResult = service.addPosicaoToCollectionIfMissing(posicaoCollection, ...posicaoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const posicao: IPosicao = sampleWithRequiredData;
        const posicao2: IPosicao = sampleWithPartialData;
        expectedResult = service.addPosicaoToCollectionIfMissing([], posicao, posicao2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(posicao);
        expect(expectedResult).toContain(posicao2);
      });

      it('should accept null and undefined values', () => {
        const posicao: IPosicao = sampleWithRequiredData;
        expectedResult = service.addPosicaoToCollectionIfMissing([], null, posicao, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(posicao);
      });

      it('should return initial array if no Posicao is added', () => {
        const posicaoCollection: IPosicao[] = [sampleWithRequiredData];
        expectedResult = service.addPosicaoToCollectionIfMissing(posicaoCollection, undefined, null);
        expect(expectedResult).toEqual(posicaoCollection);
      });
    });

    describe('comparePosicao', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePosicao(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 11868 };
        const entity2 = null;

        const compareResult1 = service.comparePosicao(entity1, entity2);
        const compareResult2 = service.comparePosicao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 11868 };
        const entity2 = { id: 14465 };

        const compareResult1 = service.comparePosicao(entity1, entity2);
        const compareResult2 = service.comparePosicao(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 11868 };
        const entity2 = { id: 11868 };

        const compareResult1 = service.comparePosicao(entity1, entity2);
        const compareResult2 = service.comparePosicao(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
