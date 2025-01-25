import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../compra.test-samples';

import { CompraFormService } from './compra-form.service';

describe('Compra Form Service', () => {
  let service: CompraFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompraFormService);
  });

  describe('Service methods', () => {
    describe('createCompraFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCompraFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantidade: expect.any(Object),
            precoCompra: expect.any(Object),
            valorTotal: expect.any(Object),
            edicao: expect.any(Object),
          }),
        );
      });

      it('passing ICompra should create a new form with FormGroup', () => {
        const formGroup = service.createCompraFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantidade: expect.any(Object),
            precoCompra: expect.any(Object),
            valorTotal: expect.any(Object),
            edicao: expect.any(Object),
          }),
        );
      });
    });

    describe('getCompra', () => {
      it('should return NewCompra for default Compra initial value', () => {
        const formGroup = service.createCompraFormGroup(sampleWithNewData);

        const compra = service.getCompra(formGroup) as any;

        expect(compra).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompra for empty Compra initial value', () => {
        const formGroup = service.createCompraFormGroup();

        const compra = service.getCompra(formGroup) as any;

        expect(compra).toMatchObject({});
      });

      it('should return ICompra', () => {
        const formGroup = service.createCompraFormGroup(sampleWithRequiredData);

        const compra = service.getCompra(formGroup) as any;

        expect(compra).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompra should not enable id FormControl', () => {
        const formGroup = service.createCompraFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompra should disable id FormControl', () => {
        const formGroup = service.createCompraFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
