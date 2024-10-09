import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../brigade.test-samples';

import { BrigadeFormService } from './brigade-form.service';

describe('Brigade Form Service', () => {
  let service: BrigadeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BrigadeFormService);
  });

  describe('Service methods', () => {
    describe('createBrigadeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBrigadeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            establishedYear: expect.any(Object),
            governorate: expect.any(Object),
          }),
        );
      });

      it('passing IBrigade should create a new form with FormGroup', () => {
        const formGroup = service.createBrigadeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            establishedYear: expect.any(Object),
            governorate: expect.any(Object),
          }),
        );
      });
    });

    describe('getBrigade', () => {
      it('should return NewBrigade for default Brigade initial value', () => {
        const formGroup = service.createBrigadeFormGroup(sampleWithNewData);

        const brigade = service.getBrigade(formGroup) as any;

        expect(brigade).toMatchObject(sampleWithNewData);
      });

      it('should return NewBrigade for empty Brigade initial value', () => {
        const formGroup = service.createBrigadeFormGroup();

        const brigade = service.getBrigade(formGroup) as any;

        expect(brigade).toMatchObject({});
      });

      it('should return IBrigade', () => {
        const formGroup = service.createBrigadeFormGroup(sampleWithRequiredData);

        const brigade = service.getBrigade(formGroup) as any;

        expect(brigade).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBrigade should not enable id FormControl', () => {
        const formGroup = service.createBrigadeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBrigade should disable id FormControl', () => {
        const formGroup = service.createBrigadeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
