import { Component, inject, input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICompra } from '../compra.model';
import { EdicaoService } from '../../edicao/service/edicao.service';

@Component({
  selector: 'jhi-compra-detail',
  templateUrl: './compra-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CompraDetailComponent implements OnInit {
  compra = input<ICompra | null>(null);

  protected readonly edicaoService = inject(EdicaoService);

  previousState(): void {
    window.history.back();
  }

  ngOnInit(): void {
    if (this.compra() && this.compra()!.edicao !== null) {
      this.edicaoService.find(this.compra()!.edicao!.id).subscribe(edicao => {
        const aux = edicao.body;
        this.compra()!.edicao = aux;
      });
    }
  }
}
