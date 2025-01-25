import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PosicaoService } from '../service/posicao.service';
import { IPosicao } from '../posicao.model';
import { PosicaoFormService } from './posicao-form.service';

import { PosicaoUpdateComponent } from './posicao-update.component';

describe('Posicao Management Update Component', () => {
  let comp: PosicaoUpdateComponent;
  let fixture: ComponentFixture<PosicaoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let posicaoFormService: PosicaoFormService;
  let posicaoService: PosicaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PosicaoUpdateComponent],
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
      .overrideTemplate(PosicaoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PosicaoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    posicaoFormService = TestBed.inject(PosicaoFormService);
    posicaoService = TestBed.inject(PosicaoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const posicao: IPosicao = { id: 14465 };

      activatedRoute.data = of({ posicao });
      comp.ngOnInit();

      expect(comp.posicao).toEqual(posicao);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPosicao>>();
      const posicao = { id: 11868 };
      jest.spyOn(posicaoFormService, 'getPosicao').mockReturnValue(posicao);
      jest.spyOn(posicaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ posicao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: posicao }));
      saveSubject.complete();

      // THEN
      expect(posicaoFormService.getPosicao).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(posicaoService.update).toHaveBeenCalledWith(expect.objectContaining(posicao));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPosicao>>();
      const posicao = { id: 11868 };
      jest.spyOn(posicaoFormService, 'getPosicao').mockReturnValue({ id: null });
      jest.spyOn(posicaoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ posicao: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: posicao }));
      saveSubject.complete();

      // THEN
      expect(posicaoFormService.getPosicao).toHaveBeenCalled();
      expect(posicaoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPosicao>>();
      const posicao = { id: 11868 };
      jest.spyOn(posicaoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ posicao });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(posicaoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
