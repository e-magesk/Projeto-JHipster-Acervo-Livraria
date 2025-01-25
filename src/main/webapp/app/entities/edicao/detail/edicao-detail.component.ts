import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IEdicao } from '../edicao.model';

@Component({
  selector: 'jhi-edicao-detail',
  templateUrl: './edicao-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class EdicaoDetailComponent {
  edicao = input<IEdicao | null>(null);

  previousState(): void {
    window.history.back();
  }
}
