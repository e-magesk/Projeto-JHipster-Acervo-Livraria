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

describe('Posicao e2e test', () => {
  const posicaoPageUrl = '/posicao';
  const posicaoPageUrlPattern = new RegExp('/posicao(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const posicaoSample = { codigo: 'U3-7-m8950' };

  let posicao;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/posicaos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/posicaos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/posicaos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (posicao) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/posicaos/${posicao.id}`,
      }).then(() => {
        posicao = undefined;
      });
    }
  });

  it('Posicaos menu should load Posicaos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('posicao');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Posicao').should('exist');
    cy.url().should('match', posicaoPageUrlPattern);
  });

  describe('Posicao page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(posicaoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Posicao page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/posicao/new$'));
        cy.getEntityCreateUpdateHeading('Posicao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', posicaoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/posicaos',
          body: posicaoSample,
        }).then(({ body }) => {
          posicao = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/posicaos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/posicaos?page=0&size=20>; rel="last",<http://localhost/api/posicaos?page=0&size=20>; rel="first"',
              },
              body: [posicao],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(posicaoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Posicao page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('posicao');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', posicaoPageUrlPattern);
      });

      it('edit button click should load edit Posicao page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Posicao');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', posicaoPageUrlPattern);
      });

      it('edit button click should load edit Posicao page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Posicao');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', posicaoPageUrlPattern);
      });

      it('last delete button click should delete instance of Posicao', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('posicao').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', posicaoPageUrlPattern);

        posicao = undefined;
      });
    });
  });

  describe('new Posicao page', () => {
    beforeEach(() => {
      cy.visit(`${posicaoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Posicao');
    });

    it('should create an instance of Posicao', () => {
      cy.get(`[data-cy="codigo"]`).type('L4-354278-z1492');
      cy.get(`[data-cy="codigo"]`).should('have.value', 'L4-354278-z1492');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        posicao = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', posicaoPageUrlPattern);
    });
  });
});
