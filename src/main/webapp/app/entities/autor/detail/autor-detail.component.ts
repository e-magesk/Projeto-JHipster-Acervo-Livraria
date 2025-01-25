import { Component, inject, input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAutor } from '../autor.model';
import { AutorService } from '../service/autor.service';
import { ILivro } from '../../livro/livro.model';

@Component({
  selector: 'jhi-autor-detail',
  templateUrl: './autor-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AutorDetailComponent implements OnInit {
  autor = input<IAutor | null>(null);

  protected readonly autorService = inject(AutorService);

  previousState(): void {
    window.history.back();
  }

  ngOnInit(): void {
    if (this.autor()) {
      this.autorService.getAutorLivros(this.autor()!.id).subscribe((livros: ILivro[]) => {
        this.autor()!.livros = livros;
      });
    }
  }
}
