import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEdicao } from 'app/entities/edicao/edicao.model';
import { EdicaoService } from 'app/entities/edicao/service/edicao.service';
import { IVenda } from '../venda.model';
import { VendaService } from '../service/venda.service';
import { VendaFormGroup, VendaFormService } from './venda-form.service';

@Component({
  selector: 'jhi-venda-update',
  templateUrl: './venda-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VendaUpdateComponent implements OnInit {
  isSaving = false;
  venda: IVenda | null = null;

  edicaosSharedCollection: IEdicao[] = [];

  protected vendaService = inject(VendaService);
  protected vendaFormService = inject(VendaFormService);
  protected edicaoService = inject(EdicaoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VendaFormGroup = this.vendaFormService.createVendaFormGroup();

  compareEdicao = (o1: IEdicao | null, o2: IEdicao | null): boolean => this.edicaoService.compareEdicao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venda }) => {
      this.venda = venda;
      if (venda) {
        this.updateForm(venda);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const venda = this.vendaFormService.getVenda(this.editForm);
    if (venda.id !== null) {
      this.subscribeToSaveResponse(this.vendaService.update(venda));
    } else {
      this.subscribeToSaveResponse(this.vendaService.create(venda));
    }
  }

  onPrecoChange(event: Event): void {
    const preco = (event.target as HTMLInputElement).value;
    if (this.editForm.get('quantidade') !== null) {
      let quantidade = this.editForm.get('quantidade')!.value;
      if (quantidade === null || quantidade === undefined) {
        quantidade = 0;
      }
      this.editForm.patchValue({
        valorTotal: quantidade * parseFloat(preco),
      });
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenda>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(venda: IVenda): void {
    this.venda = venda;
    this.vendaFormService.resetForm(this.editForm, venda);

    this.edicaosSharedCollection = this.edicaoService.addEdicaoToCollectionIfMissing<IEdicao>(this.edicaosSharedCollection, venda.edicao);
  }

  protected loadRelationshipsOptions(): void {
    this.edicaoService
      .query()
      .pipe(map((res: HttpResponse<IEdicao[]>) => res.body ?? []))
      .pipe(map((edicaos: IEdicao[]) => this.edicaoService.addEdicaoToCollectionIfMissing<IEdicao>(edicaos, this.venda?.edicao)))
      .subscribe((edicaos: IEdicao[]) => (this.edicaosSharedCollection = edicaos));
  }
}
