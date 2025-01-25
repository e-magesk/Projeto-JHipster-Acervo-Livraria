import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPosicao } from 'app/entities/posicao/posicao.model';
import { PosicaoService } from 'app/entities/posicao/service/posicao.service';
import { ILivro } from 'app/entities/livro/livro.model';
import { LivroService } from 'app/entities/livro/service/livro.service';
import { IEdicao } from '../edicao.model';
import { EdicaoService } from '../service/edicao.service';
import { EdicaoFormService } from './edicao-form.service';

import { EdicaoUpdateComponent } from './edicao-update.component';

describe('Edicao Management Update Component', () => {
  let comp: EdicaoUpdateComponent;
  let fixture: ComponentFixture<EdicaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let edicaoFormService: EdicaoFormService;
  let edicaoService: EdicaoService;
  let posicaoService: PosicaoService;
  let livroService: LivroService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EdicaoUpdateComponent],
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
      .overrideTemplate(EdicaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EdicaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    edicaoFormService = TestBed.inject(EdicaoFormService);
    edicaoService = TestBed.inject(EdicaoService);
    posicaoService = TestBed.inject(PosicaoService);
    livroService = TestBed.inject(LivroService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call posicao query and add missing value', () => {
      const edicao: IEdicao = { id: 23410 };
      const posicao: IPosicao = { id: 11868 };
      edicao.posicao = posicao;

      const posicaoCollection: IPosicao[] = [{ id: 11868 }];
      jest.spyOn(posicaoService, 'query').mockReturnValue(of(new HttpResponse({ body: posicaoCollection })));
      const expectedCollection: IPosicao[] = [posicao, ...posicaoCollection];
      jest.spyOn(posicaoService, 'addPosicaoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ edicao });
      comp.ngOnInit();

      expect(posicaoService.query).toHaveBeenCalled();
      expect(posicaoService.addPosicaoToCollectionIfMissing).toHaveBeenCalledWith(posicaoCollection, posicao);
      expect(comp.posicaosCollection).toEqual(expectedCollection);
    });

    it('Should call Livro query and add missing value', () => {
      const edicao: IEdicao = { id: 23410 };
      const livro: ILivro = { id: 16172 };
      edicao.livro = livro;

      const livroCollection: ILivro[] = [{ id: 16172 }];
      jest.spyOn(livroService, 'query').mockReturnValue(of(new HttpResponse({ body: livroCollection })));
      const additionalLivros = [livro];
      const expectedCollection: ILivro[] = [...additionalLivros, ...livroCollection];
      jest.spyOn(livroService, 'addLivroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ edicao });
      comp.ngOnInit();

      expect(livroService.query).toHaveBeenCalled();
      expect(livroService.addLivroToCollectionIfMissing).toHaveBeenCalledWith(
        livroCollection,
        ...additionalLivros.map(expect.objectContaining),
      );
      expect(comp.livrosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const edicao: IEdicao = { id: 23410 };
      const posicao: IPosicao = { id: 11868 };
      edicao.posicao = posicao;
      const livro: ILivro = { id: 16172 };
      edicao.livro = livro;

      activatedRoute.data = of({ edicao });
      comp.ngOnInit();

      expect(comp.posicaosCollection).toContainEqual(posicao);
      expect(comp.livrosSharedCollection).toContainEqual(livro);
      expect(comp.edicao).toEqual(edicao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEdicao>>();
      const edicao = { id: 32276 };
      jest.spyOn(edicaoFormService, 'getEdicao').mockReturnValue(edicao);
      jest.spyOn(edicaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ edicao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: edicao }));
      saveSubject.complete();

      // THEN
      expect(edicaoFormService.getEdicao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(edicaoService.update).toHaveBeenCalledWith(expect.objectContaining(edicao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEdicao>>();
      const edicao = { id: 32276 };
      jest.spyOn(edicaoFormService, 'getEdicao').mockReturnValue({ id: null });
      jest.spyOn(edicaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ edicao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: edicao }));
      saveSubject.complete();

      // THEN
      expect(edicaoFormService.getEdicao).toHaveBeenCalled();
      expect(edicaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEdicao>>();
      const edicao = { id: 32276 };
      jest.spyOn(edicaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ edicao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(edicaoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePosicao', () => {
      it('Should forward to posicaoService', () => {
        const entity = { id: 11868 };
        const entity2 = { id: 14465 };
        jest.spyOn(posicaoService, 'comparePosicao');
        comp.comparePosicao(entity, entity2);
        expect(posicaoService.comparePosicao).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLivro', () => {
      it('Should forward to livroService', () => {
        const entity = { id: 16172 };
        const entity2 = { id: 26070 };
        jest.spyOn(livroService, 'compareLivro');
        comp.compareLivro(entity, entity2);
        expect(livroService.compareLivro).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
