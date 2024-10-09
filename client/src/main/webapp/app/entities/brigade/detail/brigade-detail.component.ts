import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IBrigade } from '../brigade.model';

@Component({
  standalone: true,
  selector: 'jhi-brigade-detail',
  templateUrl: './brigade-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BrigadeDetailComponent {
  brigade = input<IBrigade | null>(null);

  previousState(): void {
    window.history.back();
  }
}
