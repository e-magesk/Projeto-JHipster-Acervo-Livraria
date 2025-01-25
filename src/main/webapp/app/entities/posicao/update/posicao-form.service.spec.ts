import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../posicao.test-samples';

import { PosicaoFormService } from './posicao-form.service';

describe('Posicao Form Service', () => {
  let service: PosicaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PosicaoFormService);
  });

  describe('Service methods', () => {
    describe('createPosicaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPosicaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
          }),
        );
      });

      it('passing IPosicao should create a new form with FormGroup', () => {
        const formGroup = service.createPosicaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
          }),
        );
      });
    });

    describe('getPosicao', () => {
      it('should return NewPosicao for default Posicao initial value', () => {
        const formGroup = service.createPosicaoFormGroup(sampleWithNewData);

        const posicao = service.getPosicao(formGroup) as any;

        expect(posicao).toMatchObject(sampleWithNewData);
      });

      it('should return NewPosicao for empty Posicao initial value', () => {
        const formGroup = service.createPosicaoFormGroup();

        const posicao = service.getPosicao(formGroup) as any;

        expect(posicao).toMatchObject({});
      });

      it('should return IPosicao', () => {
        const formGroup = service.createPosicaoFormGroup(sampleWithRequiredData);

        const posicao = service.getPosicao(formGroup) as any;

        expect(posicao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPosicao should not enable id FormControl', () => {
        const formGroup = service.createPosicaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPosicao should disable id FormControl', () => {
        const formGroup = service.createPosicaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
