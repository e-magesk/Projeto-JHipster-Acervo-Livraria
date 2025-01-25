import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEdicao } from '../edicao.model';
import { EdicaoService } from '../service/edicao.service';

@Component({
  templateUrl: './edicao-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EdicaoDeleteDialogComponent {
  edicao?: IEdicao;

  protected edicaoService = inject(EdicaoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.edicaoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
