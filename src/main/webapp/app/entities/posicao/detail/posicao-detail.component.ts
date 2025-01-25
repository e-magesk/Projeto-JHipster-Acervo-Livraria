import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPosicao } from '../posicao.model';

@Component({
  selector: 'jhi-posicao-detail',
  templateUrl: './posicao-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PosicaoDetailComponent {
  posicao = input<IPosicao | null>(null);

  previousState(): void {
    window.history.back();
  }
}
