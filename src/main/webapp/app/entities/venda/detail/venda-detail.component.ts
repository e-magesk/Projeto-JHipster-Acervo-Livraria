import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IVenda } from '../venda.model';

@Component({
  selector: 'jhi-venda-detail',
  templateUrl: './venda-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class VendaDetailComponent {
  venda = input<IVenda | null>(null);

  previousState(): void {
    window.history.back();
  }
}
