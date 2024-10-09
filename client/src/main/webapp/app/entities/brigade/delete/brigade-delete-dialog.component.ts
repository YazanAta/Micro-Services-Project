import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBrigade } from '../brigade.model';
import { BrigadeService } from '../service/brigade.service';

@Component({
  standalone: true,
  templateUrl: './brigade-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BrigadeDeleteDialogComponent {
  brigade?: IBrigade;

  protected brigadeService = inject(BrigadeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.brigadeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
