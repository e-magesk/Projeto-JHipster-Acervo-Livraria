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

describe('Venda e2e test', () => {
  const vendaPageUrl = '/venda';
  const vendaPageUrlPattern = new RegExp('/venda(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vendaSample = { quantidade: 16274, precoVenda: 1192.07 };

  let venda;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vendas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vendas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vendas/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (venda) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vendas/${venda.id}`,
      }).then(() => {
        venda = undefined;
      });
    }
  });

  it('Vendas menu should load Vendas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('venda');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Venda').should('exist');
    cy.url().should('match', vendaPageUrlPattern);
  });

  describe('Venda page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vendaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Venda page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/venda/new$'));
        cy.getEntityCreateUpdateHeading('Venda');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vendaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vendas',
          body: vendaSample,
        }).then(({ body }) => {
          venda = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vendas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/vendas?page=0&size=20>; rel="last",<http://localhost/api/vendas?page=0&size=20>; rel="first"',
              },
              body: [venda],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vendaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Venda page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('venda');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vendaPageUrlPattern);
      });

      it('edit button click should load edit Venda page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Venda');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vendaPageUrlPattern);
      });

      it('edit button click should load edit Venda page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Venda');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vendaPageUrlPattern);
      });

      it('last delete button click should delete instance of Venda', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('venda').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', vendaPageUrlPattern);

        venda = undefined;
      });
    });
  });

  describe('new Venda page', () => {
    beforeEach(() => {
      cy.visit(`${vendaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Venda');
    });

    it('should create an instance of Venda', () => {
      cy.get(`[data-cy="quantidade"]`).type('26900');
      cy.get(`[data-cy="quantidade"]`).should('have.value', '26900');

      cy.get(`[data-cy="precoVenda"]`).type('8834.5');
      cy.get(`[data-cy="precoVenda"]`).should('have.value', '8834.5');

      cy.get(`[data-cy="valorTotal"]`).type('29997.45');
      cy.get(`[data-cy="valorTotal"]`).should('have.value', '29997.45');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        venda = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', vendaPageUrlPattern);
    });
  });
});
