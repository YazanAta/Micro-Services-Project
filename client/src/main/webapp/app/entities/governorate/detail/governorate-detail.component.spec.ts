import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { GovernorateDetailComponent } from './governorate-detail.component';

describe('Governorate Management Detail Component', () => {
  let comp: GovernorateDetailComponent;
  let fixture: ComponentFixture<GovernorateDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GovernorateDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./governorate-detail.component').then(m => m.GovernorateDetailComponent),
              resolve: { governorate: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(GovernorateDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GovernorateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load governorate on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GovernorateDetailComponent);

      // THEN
      expect(instance.governorate()).toEqual(expect.objectContaining({ id: 123 }));
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
