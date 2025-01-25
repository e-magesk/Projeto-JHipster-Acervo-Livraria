import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../venda.test-samples';

import { VendaFormService } from './venda-form.service';

describe('Venda Form Service', () => {
  let service: VendaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VendaFormService);
  });

  describe('Service methods', () => {
    describe('createVendaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVendaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantidade: expect.any(Object),
            precoVenda: expect.any(Object),
            valorTotal: expect.any(Object),
            edicao: expect.any(Object),
          }),
        );
      });

      it('passing IVenda should create a new form with FormGroup', () => {
        const formGroup = service.createVendaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            quantidade: expect.any(Object),
            precoVenda: expect.any(Object),
            valorTotal: expect.any(Object),
            edicao: expect.any(Object),
          }),
        );
      });
    });

    describe('getVenda', () => {
      it('should return NewVenda for default Venda initial value', () => {
        const formGroup = service.createVendaFormGroup(sampleWithNewData);

        const venda = service.getVenda(formGroup) as any;

        expect(venda).toMatchObject(sampleWithNewData);
      });

      it('should return NewVenda for empty Venda initial value', () => {
        const formGroup = service.createVendaFormGroup();

        const venda = service.getVenda(formGroup) as any;

        expect(venda).toMatchObject({});
      });

      it('should return IVenda', () => {
        const formGroup = service.createVendaFormGroup(sampleWithRequiredData);

        const venda = service.getVenda(formGroup) as any;

        expect(venda).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVenda should not enable id FormControl', () => {
        const formGroup = service.createVendaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVenda should disable id FormControl', () => {
        const formGroup = service.createVendaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
