import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PosicaoDetailComponent } from './posicao-detail.component';

describe('Posicao Management Detail Component', () => {
  let comp: PosicaoDetailComponent;
  let fixture: ComponentFixture<PosicaoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PosicaoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./posicao-detail.component').then(m => m.PosicaoDetailComponent),
              resolve: { posicao: () => of({ id: 11868 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PosicaoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PosicaoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load posicao on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PosicaoDetailComponent);

      // THEN
      expect(instance.posicao()).toEqual(expect.objectContaining({ id: 11868 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
