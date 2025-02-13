import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ILivro } from 'app/entities/livro/livro.model';
import { LivroService } from 'app/entities/livro/service/livro.service';
import { AutorService } from '../service/autor.service';
import { IAutor } from '../autor.model';
import { AutorFormService } from './autor-form.service';

import { AutorUpdateComponent } from './autor-update.component';

describe('Autor Management Update Component', () => {
  let comp: AutorUpdateComponent;
  let fixture: ComponentFixture<AutorUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let autorFormService: AutorFormService;
  let autorService: AutorService;
  let livroService: LivroService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AutorUpdateComponent],
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
      .overrideTemplate(AutorUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AutorUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    autorFormService = TestBed.inject(AutorFormService);
    autorService = TestBed.inject(AutorService);
    livroService = TestBed.inject(LivroService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Livro query and add missing value', () => {
      const autor: IAutor = { id: 27814 };
      const livros: ILivro[] = [{ id: 16172 }];
      autor.livros = livros;

      const livroCollection: ILivro[] = [{ id: 16172 }];
      jest.spyOn(livroService, 'query').mockReturnValue(of(new HttpResponse({ body: livroCollection })));
      const additionalLivros = [...livros];
      const expectedCollection: ILivro[] = [...additionalLivros, ...livroCollection];
      jest.spyOn(livroService, 'addLivroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ autor });
      comp.ngOnInit();

      expect(livroService.query).toHaveBeenCalled();
      expect(livroService.addLivroToCollectionIfMissing).toHaveBeenCalledWith(
        livroCollection,
        ...additionalLivros.map(expect.objectContaining),
      );
      expect(comp.livrosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const autor: IAutor = { id: 27814 };
      const livro: ILivro = { id: 16172 };
      autor.livros = [livro];

      activatedRoute.data = of({ autor });
      comp.ngOnInit();

      expect(comp.livrosSharedCollection).toContainEqual(livro);
      expect(comp.autor).toEqual(autor);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAutor>>();
      const autor = { id: 29313 };
      jest.spyOn(autorFormService, 'getAutor').mockReturnValue(autor);
      jest.spyOn(autorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ autor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: autor }));
      saveSubject.complete();

      // THEN
      expect(autorFormService.getAutor).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(autorService.update).toHaveBeenCalledWith(expect.objectContaining(autor));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAutor>>();
      const autor = { id: 29313 };
      jest.spyOn(autorFormService, 'getAutor').mockReturnValue({ id: null });
      jest.spyOn(autorService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ autor: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: autor }));
      saveSubject.complete();

      // THEN
      expect(autorFormService.getAutor).toHaveBeenCalled();
      expect(autorService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAutor>>();
      const autor = { id: 29313 };
      jest.spyOn(autorService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ autor });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(autorService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
