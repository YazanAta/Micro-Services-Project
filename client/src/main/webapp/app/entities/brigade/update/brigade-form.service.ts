import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IBrigade, NewBrigade } from '../brigade.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBrigade for edit and NewBrigadeFormGroupInput for create.
 */
type BrigadeFormGroupInput = IBrigade | PartialWithRequiredKeyOf<NewBrigade>;

type BrigadeFormDefaults = Pick<NewBrigade, 'id'>;

type BrigadeFormGroupContent = {
  id: FormControl<IBrigade['id'] | NewBrigade['id']>;
  name: FormControl<IBrigade['name']>;
  type: FormControl<IBrigade['type']>;
  establishedYear: FormControl<IBrigade['establishedYear']>;
  governorate: FormControl<IBrigade['governorate']>;
};

export type BrigadeFormGroup = FormGroup<BrigadeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BrigadeFormService {
  createBrigadeFormGroup(brigade: BrigadeFormGroupInput = { id: null }): BrigadeFormGroup {
    const brigadeRawValue = {
      ...this.getFormDefaults(),
      ...brigade,
    };
    return new FormGroup<BrigadeFormGroupContent>({
      id: new FormControl(
        { value: brigadeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(brigadeRawValue.name, {
        validators: [Validators.required],
      }),
      type: new FormControl(brigadeRawValue.type),
      establishedYear: new FormControl(brigadeRawValue.establishedYear),
      governorate: new FormControl(brigadeRawValue.governorate, {
        validators: [Validators.required],
      }),
    });
  }

  getBrigade(form: BrigadeFormGroup): IBrigade | NewBrigade {
    return form.getRawValue() as IBrigade | NewBrigade;
  }

  resetForm(form: BrigadeFormGroup, brigade: BrigadeFormGroupInput): void {
    const brigadeRawValue = { ...this.getFormDefaults(), ...brigade };
    form.reset(
      {
        ...brigadeRawValue,
        id: { value: brigadeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BrigadeFormDefaults {
    return {
      id: null,
    };
  }
}
