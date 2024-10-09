import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { GovernorateService } from '../service/governorate.service';
import { IGovernorate } from '../governorate.model';
import { GovernorateFormService } from './governorate-form.service';

import { GovernorateUpdateComponent } from './governorate-update.component';

describe('Governorate Management Update Component', () => {
  let comp: GovernorateUpdateComponent;
  let fixture: ComponentFixture<GovernorateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let governorateFormService: GovernorateFormService;
  let governorateService: GovernorateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [GovernorateUpdateComponent],
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
      .overrideTemplate(GovernorateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GovernorateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    governorateFormService = TestBed.inject(GovernorateFormService);
    governorateService = TestBed.inject(GovernorateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const governorate: IGovernorate = { id: 456 };

      activatedRoute.data = of({ governorate });
      comp.ngOnInit();

      expect(comp.governorate).toEqual(governorate);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGovernorate>>();
      const governorate = { id: 123 };
      jest.spyOn(governorateFormService, 'getGovernorate').mockReturnValue(governorate);
      jest.spyOn(governorateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ governorate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: governorate }));
      saveSubject.complete();

      // THEN
      expect(governorateFormService.getGovernorate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(governorateService.update).toHaveBeenCalledWith(expect.objectContaining(governorate));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGovernorate>>();
      const governorate = { id: 123 };
      jest.spyOn(governorateFormService, 'getGovernorate').mockReturnValue({ id: null });
      jest.spyOn(governorateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ governorate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: governorate }));
      saveSubject.complete();

      // THEN
      expect(governorateFormService.getGovernorate).toHaveBeenCalled();
      expect(governorateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGovernorate>>();
      const governorate = { id: 123 };
      jest.spyOn(governorateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ governorate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(governorateService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
