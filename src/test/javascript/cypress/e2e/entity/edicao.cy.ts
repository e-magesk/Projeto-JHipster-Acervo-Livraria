import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Edicao e2e test', () => {
  const edicaoPageUrl = '/edicao';
  const edicaoPageUrlPattern = new RegExp('/edicao(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const edicaoSample = { editora: 'yawn but', dataLancamento: '2025-01-24', quantidadeExemplares: 24205, preco: 22296.02 };

  let edicao;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/edicaos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/edicaos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/edicaos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (edicao) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/edicaos/${edicao.id}`,
      }).then(() => {
        edicao = undefined;
      });
    }
  });

  it('Edicaos menu should load Edicaos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('edicao');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Edicao').should('exist');
    cy.url().should('match', edicaoPageUrlPattern);
  });

  describe('Edicao page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(edicaoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Edicao page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/edicao/new$'));
        cy.getEntityCreateUpdateHeading('Edicao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', edicaoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/edicaos',
          body: edicaoSample,
        }).then(({ body }) => {
          edicao = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/edicaos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/edicaos?page=0&size=20>; rel="last",<http://localhost/api/edicaos?page=0&size=20>; rel="first"',
              },
              body: [edicao],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(edicaoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Edicao page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('edicao');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', edicaoPageUrlPattern);
      });

      it('edit button click should load edit Edicao page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Edicao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', edicaoPageUrlPattern);
      });

      it('edit button click should load edit Edicao page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Edicao');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', edicaoPageUrlPattern);
      });

      it('last delete button click should delete instance of Edicao', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('edicao').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', edicaoPageUrlPattern);

        edicao = undefined;
      });
    });
  });

  describe('new Edicao page', () => {
    beforeEach(() => {
      cy.visit(`${edicaoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Edicao');
    });

    it('should create an instance of Edicao', () => {
      cy.get(`[data-cy="editora"]`).type('yet putrefy');
      cy.get(`[data-cy="editora"]`).should('have.value', 'yet putrefy');

      cy.get(`[data-cy="dataLancamento"]`).type('2025-01-24');
      cy.get(`[data-cy="dataLancamento"]`).blur();
      cy.get(`[data-cy="dataLancamento"]`).should('have.value', '2025-01-24');

      cy.get(`[data-cy="quantidadeExemplares"]`).type('29508');
      cy.get(`[data-cy="quantidadeExemplares"]`).should('have.value', '29508');

      cy.get(`[data-cy="preco"]`).type('2898.05');
      cy.get(`[data-cy="preco"]`).should('have.value', '2898.05');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        edicao = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', edicaoPageUrlPattern);
    });
  });
});
