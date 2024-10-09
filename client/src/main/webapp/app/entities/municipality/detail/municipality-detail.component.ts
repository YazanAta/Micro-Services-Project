import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMunicipality } from '../municipality.model';

@Component({
  standalone: true,
  selector: 'jhi-municipality-detail',
  templateUrl: './municipality-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MunicipalityDetailComponent {
  municipality = input<IMunicipality | null>(null);

  previousState(): void {
    window.history.back();
  }
}
