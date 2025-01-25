import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPosicao } from '../posicao.model';
import { PosicaoService } from '../service/posicao.service';
import { PosicaoFormGroup, PosicaoFormService } from './posicao-form.service';

@Component({
  selector: 'jhi-posicao-update',
  templateUrl: './posicao-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PosicaoUpdateComponent implements OnInit {
  isSaving = false;
  posicao: IPosicao | null = null;

  protected posicaoService = inject(PosicaoService);
  protected posicaoFormService = inject(PosicaoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PosicaoFormGroup = this.posicaoFormService.createPosicaoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ posicao }) => {
      this.posicao = posicao;
      if (posicao) {
        this.updateForm(posicao);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const posicao = this.posicaoFormService.getPosicao(this.editForm);
    if (posicao.id !== null) {
      this.subscribeToSaveResponse(this.posicaoService.update(posicao));
    } else {
      this.subscribeToSaveResponse(this.posicaoService.create(posicao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPosicao>>): void {
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

  protected updateForm(posicao: IPosicao): void {
    this.posicao = posicao;
    this.posicaoFormService.resetForm(this.editForm, posicao);
  }
}
