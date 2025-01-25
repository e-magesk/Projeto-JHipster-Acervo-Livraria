import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CompraDetailComponent } from './compra-detail.component';

describe('Compra Management Detail Component', () => {
  let comp: CompraDetailComponent;
  let fixture: ComponentFixture<CompraDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompraDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./compra-detail.component').then(m => m.CompraDetailComponent),
              resolve: { compra: () => of({ id: 17003 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CompraDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load compra on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CompraDetailComponent);

      // THEN
      expect(instance.compra()).toEqual(expect.objectContaining({ id: 17003 }));
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
