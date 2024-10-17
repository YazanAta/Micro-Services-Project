import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBrigade } from 'app/entities/brigade/brigade.model';
import { BrigadeService } from 'app/entities/brigade/service/brigade.service';
import { IMunicipality } from '../municipality.model';
import { MunicipalityService } from '../service/municipality.service';
import { MunicipalityFormGroup, MunicipalityFormService } from './municipality-form.service';

@Component({
  standalone: true,
  selector: 'jhi-municipality-update',
  templateUrl: './municipality-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MunicipalityUpdateComponent implements OnInit {
  isSaving = false;
  municipality: IMunicipality | null = null;

  brigadesSharedCollection: IBrigade[] = [];

  protected municipalityService = inject(MunicipalityService);
  protected municipalityFormService = inject(MunicipalityFormService);
  protected brigadeService = inject(BrigadeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MunicipalityFormGroup = this.municipalityFormService.createMunicipalityFormGroup();

  compareBrigade = (o1: IBrigade | null, o2: IBrigade | null): boolean => this.brigadeService.compareBrigade(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ municipality }) => {
      this.municipality = municipality;
      if (municipality) {
        this.updateForm(municipality);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const municipality = this.municipalityFormService.getMunicipality(this.editForm);
    if (municipality.id !== null) {
      this.subscribeToSaveResponse(this.municipalityService.update(municipality));
    } else {
      this.subscribeToSaveResponse(this.municipalityService.create(municipality));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMunicipality>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(municipality: IMunicipality): void {
    this.municipality = municipality;
    this.municipalityFormService.resetForm(this.editForm, municipality);

    this.brigadesSharedCollection = this.brigadeService.addBrigadeToCollectionIfMissing<IBrigade>(
      this.brigadesSharedCollection,
      municipality.brigade,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.brigadeService
      .query()
      .pipe(map((res: HttpResponse<IBrigade[]>) => res.body ?? []))
      .pipe(
        map((brigades: IBrigade[]) => this.brigadeService.addBrigadeToCollectionIfMissing<IBrigade>(brigades, this.municipality?.brigade)),
      )
      .subscribe((brigades: IBrigade[]) => (this.brigadesSharedCollection = brigades));
  }
}
