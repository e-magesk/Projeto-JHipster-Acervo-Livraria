import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'acervoLivrariaApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'livro',
    data: { pageTitle: 'acervoLivrariaApp.livro.home.title' },
    loadChildren: () => import('./livro/livro.routes'),
  },
  {
    path: 'edicao',
    data: { pageTitle: 'acervoLivrariaApp.edicao.home.title' },
    loadChildren: () => import('./edicao/edicao.routes'),
  },
  {
    path: 'autor',
    data: { pageTitle: 'acervoLivrariaApp.autor.home.title' },
    loadChildren: () => import('./autor/autor.routes'),
  },
  {
    path: 'posicao',
    data: { pageTitle: 'acervoLivrariaApp.posicao.home.title' },
    loadChildren: () => import('./posicao/posicao.routes'),
  },
  {
    path: 'venda',
    data: { pageTitle: 'acervoLivrariaApp.venda.home.title' },
    loadChildren: () => import('./venda/venda.routes'),
  },
  {
    path: 'compra',
    data: { pageTitle: 'acervoLivrariaApp.compra.home.title' },
    loadChildren: () => import('./compra/compra.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
