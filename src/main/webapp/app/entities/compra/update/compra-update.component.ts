import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEdicao } from 'app/entities/edicao/edicao.model';
import { EdicaoService } from 'app/entities/edicao/service/edicao.service';
import { ICompra } from '../compra.model';
import { CompraService } from '../service/compra.service';
import { CompraFormGroup, CompraFormService } from './compra-form.service';

@Component({
  selector: 'jhi-compra-update',
  templateUrl: './compra-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CompraUpdateComponent implements OnInit {
  isSaving = false;
  compra: ICompra | null = null;

  edicaosSharedCollection: IEdicao[] = [];

  protected compraService = inject(CompraService);
  protected compraFormService = inject(CompraFormService);
  protected edicaoService = inject(EdicaoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CompraFormGroup = this.compraFormService.createCompraFormGroup();

  compareEdicao = (o1: IEdicao | null, o2: IEdicao | null): boolean => this.edicaoService.compareEdicao(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compra }) => {
      this.compra = compra;
      if (compra) {
        this.updateForm(compra);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compra = this.compraFormService.getCompra(this.editForm);
    if (compra.id !== null) {
      this.subscribeToSaveResponse(this.compraService.update(compra));
    } else {
      this.subscribeToSaveResponse(this.compraService.create(compra));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompra>>): void {
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

  protected updateForm(compra: ICompra): void {
    this.compra = compra;
    this.compraFormService.resetForm(this.editForm, compra);

    this.edicaosSharedCollection = this.edicaoService.addEdicaoToCollectionIfMissing<IEdicao>(this.edicaosSharedCollection, compra.edicao);
  }

  protected loadRelationshipsOptions(): void {
    this.edicaoService
      .query()
      .pipe(map((res: HttpResponse<IEdicao[]>) => res.body ?? []))
      .pipe(map((edicaos: IEdicao[]) => this.edicaoService.addEdicaoToCollectionIfMissing<IEdicao>(edicaos, this.compra?.edicao)))
      .subscribe((edicaos: IEdicao[]) => (this.edicaosSharedCollection = edicaos));
  }
}
