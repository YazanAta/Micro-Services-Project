import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../municipality.test-samples';

import { MunicipalityFormService } from './municipality-form.service';

describe('Municipality Form Service', () => {
  let service: MunicipalityFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MunicipalityFormService);
  });

  describe('Service methods', () => {
    describe('createMunicipalityFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMunicipalityFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            brigade: expect.any(Object),
          }),
        );
      });

      it('passing IMunicipality should create a new form with FormGroup', () => {
        const formGroup = service.createMunicipalityFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            brigade: expect.any(Object),
          }),
        );
      });
    });

    describe('getMunicipality', () => {
      it('should return NewMunicipality for default Municipality initial value', () => {
        const formGroup = service.createMunicipalityFormGroup(sampleWithNewData);

        const municipality = service.getMunicipality(formGroup) as any;

        expect(municipality).toMatchObject(sampleWithNewData);
      });

      it('should return NewMunicipality for empty Municipality initial value', () => {
        const formGroup = service.createMunicipalityFormGroup();

        const municipality = service.getMunicipality(formGroup) as any;

        expect(municipality).toMatchObject({});
      });

      it('should return IMunicipality', () => {
        const formGroup = service.createMunicipalityFormGroup(sampleWithRequiredData);

        const municipality = service.getMunicipality(formGroup) as any;

        expect(municipality).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMunicipality should not enable id FormControl', () => {
        const formGroup = service.createMunicipalityFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMunicipality should disable id FormControl', () => {
        const formGroup = service.createMunicipalityFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
