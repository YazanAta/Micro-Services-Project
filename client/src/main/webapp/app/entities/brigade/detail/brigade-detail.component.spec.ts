import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BrigadeDetailComponent } from './brigade-detail.component';

describe('Brigade Management Detail Component', () => {
  let comp: BrigadeDetailComponent;
  let fixture: ComponentFixture<BrigadeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BrigadeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./brigade-detail.component').then(m => m.BrigadeDetailComponent),
              resolve: { brigade: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BrigadeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BrigadeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load brigade on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BrigadeDetailComponent);

      // THEN
      expect(instance.brigade()).toEqual(expect.objectContaining({ id: 123 }));
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
