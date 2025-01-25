import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICompra } from '../compra.model';
import { CompraService } from '../service/compra.service';

@Component({
  templateUrl: './compra-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CompraDeleteDialogComponent {
  compra?: ICompra;

  protected compraService = inject(CompraService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.compraService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
