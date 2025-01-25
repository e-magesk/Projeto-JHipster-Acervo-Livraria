import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICompra, NewCompra } from '../compra.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompra for edit and NewCompraFormGroupInput for create.
 */
type CompraFormGroupInput = ICompra | PartialWithRequiredKeyOf<NewCompra>;

type CompraFormDefaults = Pick<NewCompra, 'id'>;

type CompraFormGroupContent = {
  id: FormControl<ICompra['id'] | NewCompra['id']>;
  quantidade: FormControl<ICompra['quantidade']>;
  precoCompra: FormControl<ICompra['precoCompra']>;
  valorTotal: FormControl<ICompra['valorTotal']>;
  edicao: FormControl<ICompra['edicao']>;
};

export type CompraFormGroup = FormGroup<CompraFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompraFormService {
  createCompraFormGroup(compra: CompraFormGroupInput = { id: null }): CompraFormGroup {
    const compraRawValue = {
      ...this.getFormDefaults(),
      ...compra,
    };
    return new FormGroup<CompraFormGroupContent>({
      id: new FormControl(
        { value: compraRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      quantidade: new FormControl(compraRawValue.quantidade, {
        validators: [Validators.required],
      }),
      precoCompra: new FormControl(compraRawValue.precoCompra, {
        validators: [Validators.required],
      }),
      valorTotal: new FormControl(compraRawValue.valorTotal),
      edicao: new FormControl(compraRawValue.edicao),
    });
  }

  getCompra(form: CompraFormGroup): ICompra | NewCompra {
    return form.getRawValue() as ICompra | NewCompra;
  }

  resetForm(form: CompraFormGroup, compra: CompraFormGroupInput): void {
    const compraRawValue = { ...this.getFormDefaults(), ...compra };
    form.reset(
      {
        ...compraRawValue,
        id: { value: compraRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CompraFormDefaults {
    return {
      id: null,
    };
  }
}
