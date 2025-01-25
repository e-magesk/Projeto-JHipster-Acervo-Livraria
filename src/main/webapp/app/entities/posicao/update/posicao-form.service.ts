import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPosicao, NewPosicao } from '../posicao.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPosicao for edit and NewPosicaoFormGroupInput for create.
 */
type PosicaoFormGroupInput = IPosicao | PartialWithRequiredKeyOf<NewPosicao>;

type PosicaoFormDefaults = Pick<NewPosicao, 'id'>;

type PosicaoFormGroupContent = {
  id: FormControl<IPosicao['id'] | NewPosicao['id']>;
  codigo: FormControl<IPosicao['codigo']>;
};

export type PosicaoFormGroup = FormGroup<PosicaoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PosicaoFormService {
  createPosicaoFormGroup(posicao: PosicaoFormGroupInput = { id: null }): PosicaoFormGroup {
    const posicaoRawValue = {
      ...this.getFormDefaults(),
      ...posicao,
    };
    return new FormGroup<PosicaoFormGroupContent>({
      id: new FormControl(
        { value: posicaoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(posicaoRawValue.codigo, {
        validators: [Validators.required, Validators.pattern('^[A-Z][0-9]\\-[0-9]+\\-[a-z][0-9]+$')],
      }),
    });
  }

  getPosicao(form: PosicaoFormGroup): IPosicao | NewPosicao {
    return form.getRawValue() as IPosicao | NewPosicao;
  }

  resetForm(form: PosicaoFormGroup, posicao: PosicaoFormGroupInput): void {
    const posicaoRawValue = { ...this.getFormDefaults(), ...posicao };
    form.reset(
      {
        ...posicaoRawValue,
        id: { value: posicaoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PosicaoFormDefaults {
    return {
      id: null,
    };
  }
}
