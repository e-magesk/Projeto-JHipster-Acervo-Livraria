import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPosicao } from '../posicao.model';
import { PosicaoService } from '../service/posicao.service';

@Component({
  templateUrl: './posicao-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PosicaoDeleteDialogComponent {
  posicao?: IPosicao;

  protected posicaoService = inject(PosicaoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.posicaoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
