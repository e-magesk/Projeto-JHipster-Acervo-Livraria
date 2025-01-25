import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EdicaoDetailComponent } from './edicao-detail.component';

describe('Edicao Management Detail Component', () => {
  let comp: EdicaoDetailComponent;
  let fixture: ComponentFixture<EdicaoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EdicaoDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./edicao-detail.component').then(m => m.EdicaoDetailComponent),
              resolve: { edicao: () => of({ id: 32276 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EdicaoDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EdicaoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load edicao on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EdicaoDetailComponent);

      // THEN
      expect(instance.edicao()).toEqual(expect.objectContaining({ id: 32276 }));
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
