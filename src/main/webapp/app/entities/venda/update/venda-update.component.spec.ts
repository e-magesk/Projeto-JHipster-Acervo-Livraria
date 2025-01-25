import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEdicao } from 'app/entities/edicao/edicao.model';
import { EdicaoService } from 'app/entities/edicao/service/edicao.service';
import { VendaService } from '../service/venda.service';
import { IVenda } from '../venda.model';
import { VendaFormService } from './venda-form.service';

import { VendaUpdateComponent } from './venda-update.component';

describe('Venda Management Update Component', () => {
  let comp: VendaUpdateComponent;
  let fixture: ComponentFixture<VendaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vendaFormService: VendaFormService;
  let vendaService: VendaService;
  let edicaoService: EdicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VendaUpdateComponent],
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
      .overrideTemplate(VendaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VendaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vendaFormService = TestBed.inject(VendaFormService);
    vendaService = TestBed.inject(VendaService);
    edicaoService = TestBed.inject(EdicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Edicao query and add missing value', () => {
      const venda: IVenda = { id: 27942 };
      const edicao: IEdicao = { id: 32276 };
      venda.edicao = edicao;

      const edicaoCollection: IEdicao[] = [{ id: 32276 }];
      jest.spyOn(edicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: edicaoCollection })));
      const additionalEdicaos = [edicao];
      const expectedCollection: IEdicao[] = [...additionalEdicaos, ...edicaoCollection];
      jest.spyOn(edicaoService, 'addEdicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ venda });
      comp.ngOnInit();

      expect(edicaoService.query).toHaveBeenCalled();
      expect(edicaoService.addEdicaoToCollectionIfMissing).toHaveBeenCalledWith(
        edicaoCollection,
        ...additionalEdicaos.map(expect.objectContaining),
      );
      expect(comp.edicaosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const venda: IVenda = { id: 27942 };
      const edicao: IEdicao = { id: 32276 };
      venda.edicao = edicao;

      activatedRoute.data = of({ venda });
      comp.ngOnInit();

      expect(comp.edicaosSharedCollection).toContainEqual(edicao);
      expect(comp.venda).toEqual(venda);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVenda>>();
      const venda = { id: 31753 };
      jest.spyOn(vendaFormService, 'getVenda').mockReturnValue(venda);
      jest.spyOn(vendaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venda });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venda }));
      saveSubject.complete();

      // THEN
      expect(vendaFormService.getVenda).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vendaService.update).toHaveBeenCalledWith(expect.objectContaining(venda));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVenda>>();
      const venda = { id: 31753 };
      jest.spyOn(vendaFormService, 'getVenda').mockReturnValue({ id: null });
      jest.spyOn(vendaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venda: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venda }));
      saveSubject.complete();

      // THEN
      expect(vendaFormService.getVenda).toHaveBeenCalled();
      expect(vendaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVenda>>();
      const venda = { id: 31753 };
      jest.spyOn(vendaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venda });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vendaService.update).toHaveBeenCalled();
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
