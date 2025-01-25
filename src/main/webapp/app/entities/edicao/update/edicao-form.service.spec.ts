import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../edicao.test-samples';

import { EdicaoFormService } from './edicao-form.service';

describe('Edicao Form Service', () => {
  let service: EdicaoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EdicaoFormService);
  });

  describe('Service methods', () => {
    describe('createEdicaoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEdicaoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            editora: expect.any(Object),
            dataLancamento: expect.any(Object),
            quantidadeExemplares: expect.any(Object),
            preco: expect.any(Object),
            posicao: expect.any(Object),
            livro: expect.any(Object),
          }),
        );
      });

      it('passing IEdicao should create a new form with FormGroup', () => {
        const formGroup = service.createEdicaoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            editora: expect.any(Object),
            dataLancamento: expect.any(Object),
            quantidadeExemplares: expect.any(Object),
            preco: expect.any(Object),
            posicao: expect.any(Object),
            livro: expect.any(Object),
          }),
        );
      });
    });

    describe('getEdicao', () => {
      it('should return NewEdicao for default Edicao initial value', () => {
        const formGroup = service.createEdicaoFormGroup(sampleWithNewData);

        const edicao = service.getEdicao(formGroup) as any;

        expect(edicao).toMatchObject(sampleWithNewData);
      });

      it('should return NewEdicao for empty Edicao initial value', () => {
        const formGroup = service.createEdicaoFormGroup();

        const edicao = service.getEdicao(formGroup) as any;

        expect(edicao).toMatchObject({});
      });

      it('should return IEdicao', () => {
        const formGroup = service.createEdicaoFormGroup(sampleWithRequiredData);

        const edicao = service.getEdicao(formGroup) as any;

        expect(edicao).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEdicao should not enable id FormControl', () => {
        const formGroup = service.createEdicaoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEdicao should disable id FormControl', () => {
        const formGroup = service.createEdicaoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
