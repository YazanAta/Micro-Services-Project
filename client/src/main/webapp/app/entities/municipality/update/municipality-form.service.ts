import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMunicipality, NewMunicipality } from '../municipality.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMunicipality for edit and NewMunicipalityFormGroupInput for create.
 */
type MunicipalityFormGroupInput = IMunicipality | PartialWithRequiredKeyOf<NewMunicipality>;

type MunicipalityFormDefaults = Pick<NewMunicipality, 'id'>;

type MunicipalityFormGroupContent = {
  id: FormControl<IMunicipality['id'] | NewMunicipality['id']>;
  name: FormControl<IMunicipality['name']>;
  brigade: FormControl<IMunicipality['brigade']>;
};

export type MunicipalityFormGroup = FormGroup<MunicipalityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MunicipalityFormService {
  createMunicipalityFormGroup(municipality: MunicipalityFormGroupInput = { id: null }): MunicipalityFormGroup {
    const municipalityRawValue = {
      ...this.getFormDefaults(),
      ...municipality,
    };
    return new FormGroup<MunicipalityFormGroupContent>({
      id: new FormControl(
        { value: municipalityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(municipalityRawValue.name, {
        validators: [Validators.required],
      }),
      brigade: new FormControl(municipalityRawValue.brigade, {
        validators: [Validators.required],
      }),
    });
  }

  getMunicipality(form: MunicipalityFormGroup): IMunicipality | NewMunicipality {
    return form.getRawValue() as IMunicipality | NewMunicipality;
  }

  resetForm(form: MunicipalityFormGroup, municipality: MunicipalityFormGroupInput): void {
    const municipalityRawValue = { ...this.getFormDefaults(), ...municipality };
    form.reset(
      {
        ...municipalityRawValue,
        id: { value: municipalityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MunicipalityFormDefaults {
    return {
      id: null,
    };
  }
}
