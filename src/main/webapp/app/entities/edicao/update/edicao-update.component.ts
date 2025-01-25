import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPosicao } from 'app/entities/posicao/posicao.model';
import { PosicaoService } from 'app/entities/posicao/service/posicao.service';
import { ILivro } from 'app/entities/livro/livro.model';
import { LivroService } from 'app/entities/livro/service/livro.service';
import { EdicaoService } from '../service/edicao.service';
import { IEdicao } from '../edicao.model';
import { EdicaoFormGroup, EdicaoFormService } from './edicao-form.service';

@Component({
  selector: 'jhi-edicao-update',
  templateUrl: './edicao-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EdicaoUpdateComponent implements OnInit {
  isSaving = false;
  edicao: IEdicao | null = null;

  posicaosCollection: IPosicao[] = [];
  livrosSharedCollection: ILivro[] = [];

  protected edicaoService = inject(EdicaoService);
  protected edicaoFormService = inject(EdicaoFormService);
  protected posicaoService = inject(PosicaoService);
  protected livroService = inject(LivroService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EdicaoFormGroup = this.edicaoFormService.createEdicaoFormGroup();

  comparePosicao = (o1: IPosicao | null, o2: IPosicao | null): boolean => this.posicaoService.comparePosicao(o1, o2);

  compareLivro = (o1: ILivro | null, o2: ILivro | null): boolean => this.livroService.compareLivro(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ edicao }) => {
      this.edicao = edicao;
      if (edicao) {
        this.updateForm(edicao);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const edicao = this.edicaoFormService.getEdicao(this.editForm);
    if (edicao.id !== null) {
      this.subscribeToSaveResponse(this.edicaoService.update(edicao));
    } else {
      this.subscribeToSaveResponse(this.edicaoService.create(edicao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEdicao>>): void {
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

  protected updateForm(edicao: IEdicao): void {
    this.edicao = edicao;
    this.edicaoFormService.resetForm(this.editForm, edicao);

    this.posicaosCollection = this.posicaoService.addPosicaoToCollectionIfMissing<IPosicao>(this.posicaosCollection, edicao.posicao);
    this.livrosSharedCollection = this.livroService.addLivroToCollectionIfMissing<ILivro>(this.livrosSharedCollection, edicao.livro);
  }

  protected loadRelationshipsOptions(): void {
    this.posicaoService
      .query({ filter: 'edicao-is-null' })
      .pipe(map((res: HttpResponse<IPosicao[]>) => res.body ?? []))
      .pipe(map((posicaos: IPosicao[]) => this.posicaoService.addPosicaoToCollectionIfMissing<IPosicao>(posicaos, this.edicao?.posicao)))
      .subscribe((posicaos: IPosicao[]) => (this.posicaosCollection = posicaos));

    this.livroService
      .query()
      .pipe(map((res: HttpResponse<ILivro[]>) => res.body ?? []))
      .pipe(map((livros: ILivro[]) => this.livroService.addLivroToCollectionIfMissing<ILivro>(livros, this.edicao?.livro)))
      .subscribe((livros: ILivro[]) => (this.livrosSharedCollection = livros));
  }
}
