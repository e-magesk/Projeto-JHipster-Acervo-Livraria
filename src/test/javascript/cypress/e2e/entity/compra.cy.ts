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

describe('Compra e2e test', () => {
  const compraPageUrl = '/compra';
  const compraPageUrlPattern = new RegExp('/compra(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const compraSample = { quantidade: 19695, precoCompra: 29938.24 };

  let compra;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/compras+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/compras').as('postEntityRequest');
    cy.intercept('DELETE', '/api/compras/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (compra) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/compras/${compra.id}`,
      }).then(() => {
        compra = undefined;
      });
    }
  });

  it('Compras menu should load Compras page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('compra');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Compra').should('exist');
    cy.url().should('match', compraPageUrlPattern);
  });

  describe('Compra page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(compraPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Compra page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/compra/new$'));
        cy.getEntityCreateUpdateHeading('Compra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', compraPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/compras',
          body: compraSample,
        }).then(({ body }) => {
          compra = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/compras+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/compras?page=0&size=20>; rel="last",<http://localhost/api/compras?page=0&size=20>; rel="first"',
              },
              body: [compra],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(compraPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Compra page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('compra');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', compraPageUrlPattern);
      });

      it('edit button click should load edit Compra page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Compra');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', compraPageUrlPattern);
      });

      it('edit button click should load edit Compra page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Compra');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', compraPageUrlPattern);
      });

      it('last delete button click should delete instance of Compra', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('compra').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', compraPageUrlPattern);

        compra = undefined;
      });
    });
  });

  describe('new Compra page', () => {
    beforeEach(() => {
      cy.visit(`${compraPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Compra');
    });

    it('should create an instance of Compra', () => {
      cy.get(`[data-cy="quantidade"]`).type('21969');
      cy.get(`[data-cy="quantidade"]`).should('have.value', '21969');

      cy.get(`[data-cy="precoCompra"]`).type('14063');
      cy.get(`[data-cy="precoCompra"]`).should('have.value', '14063');

      cy.get(`[data-cy="valorTotal"]`).type('6767.95');
      cy.get(`[data-cy="valorTotal"]`).should('have.value', '6767.95');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        compra = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', compraPageUrlPattern);
    });
  });
});
