import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IGovernorate } from 'app/entities/governorate/governorate.model';
import { GovernorateService } from 'app/entities/governorate/service/governorate.service';
import { IBrigade } from '../brigade.model';
import { BrigadeService } from '../service/brigade.service';
import { BrigadeFormGroup, BrigadeFormService } from './brigade-form.service';

@Component({
  standalone: true,
  selector: 'jhi-brigade-update',
  templateUrl: './brigade-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BrigadeUpdateComponent implements OnInit {
  isSaving = false;
  brigade: IBrigade | null = null;

  governoratesSharedCollection: IGovernorate[] = [];

  protected brigadeService = inject(BrigadeService);
  protected brigadeFormService = inject(BrigadeFormService);
  protected governorateService = inject(GovernorateService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BrigadeFormGroup = this.brigadeFormService.createBrigadeFormGroup();

  compareGovernorate = (o1: IGovernorate | null, o2: IGovernorate | null): boolean => this.governorateService.compareGovernorate(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ brigade }) => {
      this.brigade = brigade;
      if (brigade) {
        this.updateForm(brigade);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const brigade = this.brigadeFormService.getBrigade(this.editForm);
    if (brigade.id !== null) {
      this.subscribeToSaveResponse(this.brigadeService.update(brigade));
    } else {
      this.subscribeToSaveResponse(this.brigadeService.create(brigade));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBrigade>>): void {
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

  protected updateForm(brigade: IBrigade): void {
    this.brigade = brigade;
    this.brigadeFormService.resetForm(this.editForm, brigade);

    this.governoratesSharedCollection = this.governorateService.addGovernorateToCollectionIfMissing<IGovernorate>(
      this.governoratesSharedCollection,
      brigade.governorate,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.governorateService
      .query()
      .pipe(map((res: HttpResponse<IGovernorate[]>) => res.body ?? []))
      .pipe(
        map((governorates: IGovernorate[]) =>
          this.governorateService.addGovernorateToCollectionIfMissing<IGovernorate>(governorates, this.brigade?.governorate),
        ),
      )
      .subscribe((governorates: IGovernorate[]) => (this.governoratesSharedCollection = governorates));
  }
}
