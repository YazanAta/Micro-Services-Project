import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IBrigade } from 'app/entities/brigade/brigade.model';
import { BrigadeService } from 'app/entities/brigade/service/brigade.service';
import { MunicipalityService } from '../service/municipality.service';
import { IMunicipality } from '../municipality.model';
import { MunicipalityFormService } from './municipality-form.service';

import { MunicipalityUpdateComponent } from './municipality-update.component';

describe('Municipality Management Update Component', () => {
  let comp: MunicipalityUpdateComponent;
  let fixture: ComponentFixture<MunicipalityUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let municipalityFormService: MunicipalityFormService;
  let municipalityService: MunicipalityService;
  let brigadeService: BrigadeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MunicipalityUpdateComponent],
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
      .overrideTemplate(MunicipalityUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MunicipalityUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    municipalityFormService = TestBed.inject(MunicipalityFormService);
    municipalityService = TestBed.inject(MunicipalityService);
    brigadeService = TestBed.inject(BrigadeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Brigade query and add missing value', () => {
      const municipality: IMunicipality = { id: 456 };
      const brigades: IBrigade = { id: 22265 };
      municipality.brigades = brigades;

      const brigadeCollection: IBrigade[] = [{ id: 10255 }];
      jest.spyOn(brigadeService, 'query').mockReturnValue(of(new HttpResponse({ body: brigadeCollection })));
      const additionalBrigades = [brigades];
      const expectedCollection: IBrigade[] = [...additionalBrigades, ...brigadeCollection];
      jest.spyOn(brigadeService, 'addBrigadeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ municipality });
      comp.ngOnInit();

      expect(brigadeService.query).toHaveBeenCalled();
      expect(brigadeService.addBrigadeToCollectionIfMissing).toHaveBeenCalledWith(
        brigadeCollection,
        ...additionalBrigades.map(expect.objectContaining),
      );
      expect(comp.brigadesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const municipality: IMunicipality = { id: 456 };
      const brigades: IBrigade = { id: 31692 };
      municipality.brigades = brigades;

      activatedRoute.data = of({ municipality });
      comp.ngOnInit();

      expect(comp.brigadesSharedCollection).toContain(brigades);
      expect(comp.municipality).toEqual(municipality);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMunicipality>>();
      const municipality = { id: 123 };
      jest.spyOn(municipalityFormService, 'getMunicipality').mockReturnValue(municipality);
      jest.spyOn(municipalityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ municipality });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: municipality }));
      saveSubject.complete();

      // THEN
      expect(municipalityFormService.getMunicipality).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(municipalityService.update).toHaveBeenCalledWith(expect.objectContaining(municipality));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMunicipality>>();
      const municipality = { id: 123 };
      jest.spyOn(municipalityFormService, 'getMunicipality').mockReturnValue({ id: null });
      jest.spyOn(municipalityService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ municipality: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: municipality }));
      saveSubject.complete();

      // THEN
      expect(municipalityFormService.getMunicipality).toHaveBeenCalled();
      expect(municipalityService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMunicipality>>();
      const municipality = { id: 123 };
      jest.spyOn(municipalityService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ municipality });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(municipalityService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBrigade', () => {
      it('Should forward to brigadeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(brigadeService, 'compareBrigade');
        comp.compareBrigade(entity, entity2);
        expect(brigadeService.compareBrigade).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
