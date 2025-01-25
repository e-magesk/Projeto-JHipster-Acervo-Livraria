import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICompra } from '../compra.model';

@Component({
  selector: 'jhi-compra-detail',
  templateUrl: './compra-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CompraDetailComponent {
  compra = input<ICompra | null>(null);

  previousState(): void {
    window.history.back();
  }
}
