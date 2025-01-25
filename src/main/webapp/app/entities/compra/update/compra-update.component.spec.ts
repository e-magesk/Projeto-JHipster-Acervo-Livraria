import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEdicao } from 'app/entities/edicao/edicao.model';
import { EdicaoService } from 'app/entities/edicao/service/edicao.service';
import { CompraService } from '../service/compra.service';
import { ICompra } from '../compra.model';
import { CompraFormService } from './compra-form.service';

import { CompraUpdateComponent } from './compra-update.component';

describe('Compra Management Update Component', () => {
  let comp: CompraUpdateComponent;
  let fixture: ComponentFixture<CompraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compraFormService: CompraFormService;
  let compraService: CompraService;
  let edicaoService: EdicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CompraUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CompraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compraFormService = TestBed.inject(CompraFormService);
    compraService = TestBed.inject(CompraService);
    edicaoService = TestBed.inject(EdicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Edicao query and add missing value', () => {
      const compra: ICompra = { id: 7420 };
      const edicao: IEdicao = { id: 32276 };
      compra.edicao = edicao;

      const edicaoCollection: IEdicao[] = [{ id: 32276 }];
      jest.spyOn(edicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: edicaoCollection })));
      const additionalEdicaos = [edicao];
      const expectedCollection: IEdicao[] = [...additionalEdicaos, ...edicaoCollection];
      jest.spyOn(edicaoService, 'addEdicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      expect(edicaoService.query).toHaveBeenCalled();
      expect(edicaoService.addEdicaoToCollectionIfMissing).toHaveBeenCalledWith(
        edicaoCollection,
        ...additionalEdicaos.map(expect.objectContaining),
      );
      expect(comp.edicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const compra: ICompra = { id: 7420 };
      const edicao: IEdicao = { id: 32276 };
      compra.edicao = edicao;

      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      expect(comp.edicaosSharedCollection).toContainEqual(edicao);
      expect(comp.compra).toEqual(compra);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompra>>();
      const compra = { id: 17003 };
      jest.spyOn(compraFormService, 'getCompra').mockReturnValue(compra);
      jest.spyOn(compraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compra }));
      saveSubject.complete();

      // THEN
      expect(compraFormService.getCompra).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(compraService.update).toHaveBeenCalledWith(expect.objectContaining(compra));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompra>>();
      const compra = { id: 17003 };
      jest.spyOn(compraFormService, 'getCompra').mockReturnValue({ id: null });
      jest.spyOn(compraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compra: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compra }));
      saveSubject.complete();

      // THEN
      expect(compraFormService.getCompra).toHaveBeenCalled();
      expect(compraService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompra>>();
      const compra = { id: 17003 };
      jest.spyOn(compraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compraService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEdicao', () => {
      it('Should forward to edicaoService', () => {
        const entity = { id: 32276 };
        const entity2 = { id: 23410 };
        jest.spyOn(edicaoService, 'compareEdicao');
        comp.compareEdicao(entity, entity2);
        expect(edicaoService.compareEdicao).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
