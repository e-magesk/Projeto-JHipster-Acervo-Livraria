import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEdicao, NewEdicao } from '../edicao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEdicao for edit and NewEdicaoFormGroupInput for create.
 */
type EdicaoFormGroupInput = IEdicao | PartialWithRequiredKeyOf<NewEdicao>;

type EdicaoFormDefaults = Pick<NewEdicao, 'id'>;

type EdicaoFormGroupContent = {
  id: FormControl<IEdicao['id'] | NewEdicao['id']>;
  editora: FormControl<IEdicao['editora']>;
  dataLancamento: FormControl<IEdicao['dataLancamento']>;
  quantidadeExemplares: FormControl<IEdicao['quantidadeExemplares']>;
  preco: FormControl<IEdicao['preco']>;
  posicao: FormControl<IEdicao['posicao']>;
  livro: FormControl<IEdicao['livro']>;
};

export type EdicaoFormGroup = FormGroup<EdicaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EdicaoFormService {
  createEdicaoFormGroup(edicao: EdicaoFormGroupInput = { id: null }): EdicaoFormGroup {
    const edicaoRawValue = {
      ...this.getFormDefaults(),
      ...edicao,
    };
    return new FormGroup<EdicaoFormGroupContent>({
      id: new FormControl(
        { value: edicaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      editora: new FormControl(edicaoRawValue.editora, {
        validators: [Validators.required],
      }),
      dataLancamento: new FormControl(edicaoRawValue.dataLancamento, {
        validators: [Validators.required],
      }),
      quantidadeExemplares: new FormControl(edicaoRawValue.quantidadeExemplares, {
        validators: [Validators.required],
      }),
      preco: new FormControl(edicaoRawValue.preco, {
        validators: [Validators.required],
      }),
      posicao: new FormControl(edicaoRawValue.posicao),
      livro: new FormControl(edicaoRawValue.livro),
    });
  }

  getEdicao(form: EdicaoFormGroup): IEdicao | NewEdicao {
    return form.getRawValue() as IEdicao | NewEdicao;
  }

  resetForm(form: EdicaoFormGroup, edicao: EdicaoFormGroupInput): void {
    const edicaoRawValue = { ...this.getFormDefaults(), ...edicao };
    form.reset(
      {
        ...edicaoRawValue,
        id: { value: edicaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EdicaoFormDefaults {
    return {
      id: null,
    };
  }
}
