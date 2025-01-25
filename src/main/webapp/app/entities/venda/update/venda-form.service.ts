import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVenda, NewVenda } from '../venda.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVenda for edit and NewVendaFormGroupInput for create.
 */
type VendaFormGroupInput = IVenda | PartialWithRequiredKeyOf<NewVenda>;

type VendaFormDefaults = Pick<NewVenda, 'id'>;

type VendaFormGroupContent = {
  id: FormControl<IVenda['id'] | NewVenda['id']>;
  quantidade: FormControl<IVenda['quantidade']>;
  precoVenda: FormControl<IVenda['precoVenda']>;
  valorTotal: FormControl<IVenda['valorTotal']>;
  edicao: FormControl<IVenda['edicao']>;
};

export type VendaFormGroup = FormGroup<VendaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VendaFormService {
  createVendaFormGroup(venda: VendaFormGroupInput = { id: null }): VendaFormGroup {
    const vendaRawValue = {
      ...this.getFormDefaults(),
      ...venda,
    };
    return new FormGroup<VendaFormGroupContent>({
      id: new FormControl(
        { value: vendaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantidade: new FormControl(vendaRawValue.quantidade, {
        validators: [Validators.required],
      }),
      precoVenda: new FormControl(vendaRawValue.precoVenda, {
        validators: [Validators.required],
      }),
      valorTotal: new FormControl(vendaRawValue.valorTotal),
      edicao: new FormControl(vendaRawValue.edicao),
    });
  }

  getVenda(form: VendaFormGroup): IVenda | NewVenda {
    return form.getRawValue() as IVenda | NewVenda;
  }

  resetForm(form: VendaFormGroup, venda: VendaFormGroupInput): void {
    const vendaRawValue = { ...this.getFormDefaults(), ...venda };
    form.reset(
      {
        ...vendaRawValue,
        id: { value: vendaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VendaFormDefaults {
    return {
      id: null,
    };
  }
}
