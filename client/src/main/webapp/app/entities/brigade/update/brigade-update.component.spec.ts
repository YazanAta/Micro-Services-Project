import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IGovernorate } from 'app/entities/governorate/governorate.model';
import { GovernorateService } from 'app/entities/governorate/service/governorate.service';
import { BrigadeService } from '../service/brigade.service';
import { IBrigade } from '../brigade.model';
import { BrigadeFormService } from './brigade-form.service';

import { BrigadeUpdateComponent } from './brigade-update.component';

describe('Brigade Management Update Component', () => {
  let comp: BrigadeUpdateComponent;
  let fixture: ComponentFixture<BrigadeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let brigadeFormService: BrigadeFormService;
  let brigadeService: BrigadeService;
  let governorateService: GovernorateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BrigadeUpdateComponent],
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
      .overrideTemplate(BrigadeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BrigadeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    brigadeFormService = TestBed.inject(BrigadeFormService);
    brigadeService = TestBed.inject(BrigadeService);
    governorateService = TestBed.inject(GovernorateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Governorate query and add missing value', () => {
      const brigade: IBrigade = { id: 456 };
      const governorate: IGovernorate = { id: 30990 };
      brigade.governorate = governorate;

      const governorateCollection: IGovernorate[] = [{ id: 12839 }];
      jest.spyOn(governorateService, 'query').mockReturnValue(of(new HttpResponse({ body: governorateCollection })));
      const additionalGovernorates = [governorate];
      const expectedCollection: IGovernorate[] = [...additionalGovernorates, ...governorateCollection];
      jest.spyOn(governorateService, 'addGovernorateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ brigade });
      comp.ngOnInit();

      expect(governorateService.query).toHaveBeenCalled();
      expect(governorateService.addGovernorateToCollectionIfMissing).toHaveBeenCalledWith(
        governorateCollection,
        ...additionalGovernorates.map(expect.objectContaining),
      );
      expect(comp.governoratesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const brigade: IBrigade = { id: 456 };
      const governorate: IGovernorate = { id: 15938 };
      brigade.governorate = governorate;

      activatedRoute.data = of({ brigade });
      comp.ngOnInit();

      expect(comp.governoratesSharedCollection).toContain(governorate);
      expect(comp.brigade).toEqual(brigade);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBrigade>>();
      const brigade = { id: 123 };
      jest.spyOn(brigadeFormService, 'getBrigade').mockReturnValue(brigade);
      jest.spyOn(brigadeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ brigade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: brigade }));
      saveSubject.complete();

      // THEN
      expect(brigadeFormService.getBrigade).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(brigadeService.update).toHaveBeenCalledWith(expect.objectContaining(brigade));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBrigade>>();
      const brigade = { id: 123 };
      jest.spyOn(brigadeFormService, 'getBrigade').mockReturnValue({ id: null });
      jest.spyOn(brigadeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ brigade: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: brigade }));
      saveSubject.complete();

      // THEN
      expect(brigadeFormService.getBrigade).toHaveBeenCalled();
      expect(brigadeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBrigade>>();
      const brigade = { id: 123 };
      jest.spyOn(brigadeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ brigade });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(brigadeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareGovernorate', () => {
      it('Should forward to governorateService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(governorateService, 'compareGovernorate');
        comp.compareGovernorate(entity, entity2);
        expect(governorateService.compareGovernorate).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
