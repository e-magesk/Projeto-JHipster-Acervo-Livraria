import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICompra } from '../compra.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../compra.test-samples';

import { CompraService } from './compra.service';

const requireRestSample: ICompra = {
  ...sampleWithRequiredData,
};

describe('Compra Service', () => {
  let service: CompraService;
  let httpMock: HttpTestingController;
  let expectedResult: ICompra | ICompra[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CompraService);
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

    it('should create a Compra', () => {
      const compra = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(compra).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Compra', () => {
      const compra = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(compra).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Compra', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Compra', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Compra', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCompraToCollectionIfMissing', () => {
      it('should add a Compra to an empty array', () => {
        const compra: ICompra = sampleWithRequiredData;
        expectedResult = service.addCompraToCollectionIfMissing([], compra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compra);
      });

      it('should not add a Compra to an array that contains it', () => {
        const compra: ICompra = sampleWithRequiredData;
        const compraCollection: ICompra[] = [
          {
            ...compra,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCompraToCollectionIfMissing(compraCollection, compra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Compra to an array that doesn't contain it", () => {
        const compra: ICompra = sampleWithRequiredData;
        const compraCollection: ICompra[] = [sampleWithPartialData];
        expectedResult = service.addCompraToCollectionIfMissing(compraCollection, compra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compra);
      });

      it('should add only unique Compra to an array', () => {
        const compraArray: ICompra[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const compraCollection: ICompra[] = [sampleWithRequiredData];
        expectedResult = service.addCompraToCollectionIfMissing(compraCollection, ...compraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compra: ICompra = sampleWithRequiredData;
        const compra2: ICompra = sampleWithPartialData;
        expectedResult = service.addCompraToCollectionIfMissing([], compra, compra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compra);
        expect(expectedResult).toContain(compra2);
      });

      it('should accept null and undefined values', () => {
        const compra: ICompra = sampleWithRequiredData;
        expectedResult = service.addCompraToCollectionIfMissing([], null, compra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compra);
      });

      it('should return initial array if no Compra is added', () => {
        const compraCollection: ICompra[] = [sampleWithRequiredData];
        expectedResult = service.addCompraToCollectionIfMissing(compraCollection, undefined, null);
        expect(expectedResult).toEqual(compraCollection);
      });
    });

    describe('compareCompra', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCompra(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 17003 };
        const entity2 = null;

        const compareResult1 = service.compareCompra(entity1, entity2);
        const compareResult2 = service.compareCompra(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 17003 };
        const entity2 = { id: 7420 };

        const compareResult1 = service.compareCompra(entity1, entity2);
        const compareResult2 = service.compareCompra(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 17003 };
        const entity2 = { id: 17003 };

        const compareResult1 = service.compareCompra(entity1, entity2);
        const compareResult2 = service.compareCompra(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
