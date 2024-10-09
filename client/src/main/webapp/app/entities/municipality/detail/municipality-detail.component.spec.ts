import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MunicipalityDetailComponent } from './municipality-detail.component';

describe('Municipality Management Detail Component', () => {
  let comp: MunicipalityDetailComponent;
  let fixture: ComponentFixture<MunicipalityDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MunicipalityDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./municipality-detail.component').then(m => m.MunicipalityDetailComponent),
              resolve: { municipality: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MunicipalityDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MunicipalityDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load municipality on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MunicipalityDetailComponent);

      // THEN
      expect(instance.municipality()).toEqual(expect.objectContaining({ id: 123 }));
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
