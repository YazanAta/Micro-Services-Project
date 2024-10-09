import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IGovernorate } from '../governorate.model';

@Component({
  standalone: true,
  selector: 'jhi-governorate-detail',
  templateUrl: './governorate-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class GovernorateDetailComponent {
  governorate = input<IGovernorate | null>(null);

  previousState(): void {
    window.history.back();
  }
}
