import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAutor, NewAutor } from '../autor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAutor for edit and NewAutorFormGroupInput for create.
 */
type AutorFormGroupInput = IAutor | PartialWithRequiredKeyOf<NewAutor>;

type AutorFormDefaults = Pick<NewAutor, 'id'>;

type AutorFormGroupContent = {
  id: FormControl<IAutor['id'] | NewAutor['id']>;
  nome: FormControl<IAutor['nome']>;
  nacionalidade: FormControl<IAutor['nacionalidade']>;
  livro: FormControl<IAutor['livro']>;
};

export type AutorFormGroup = FormGroup<AutorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AutorFormService {
  createAutorFormGroup(autor: AutorFormGroupInput = { id: null }): AutorFormGroup {
    const autorRawValue = {
      ...this.getFormDefaults(),
      ...autor,
    };
    return new FormGroup<AutorFormGroupContent>({
      id: new FormControl(
        { value: autorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(autorRawValue.nome),
      nacionalidade: new FormControl(autorRawValue.nacionalidade),
      livro: new FormControl(autorRawValue.livro),
    });
  }

  getAutor(form: AutorFormGroup): IAutor | NewAutor {
    return form.getRawValue() as IAutor | NewAutor;
  }

  resetForm(form: AutorFormGroup, autor: AutorFormGroupInput): void {
    const autorRawValue = { ...this.getFormDefaults(), ...autor };
    form.reset(
      {
        ...autorRawValue,
        id: { value: autorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AutorFormDefaults {
    return {
      id: null,
    };
  }
}
