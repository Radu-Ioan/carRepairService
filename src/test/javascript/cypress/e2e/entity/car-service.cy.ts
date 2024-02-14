import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('CarService e2e test', () => {
  const carServicePageUrl = '/car-service';
  const carServicePageUrlPattern = new RegExp('/car-service(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const carServiceSample = { address: 'vivacious wonderfully vice' };

  let carService;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/car-services+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/car-services').as('postEntityRequest');
    cy.intercept('DELETE', '/api/car-services/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (carService) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/car-services/${carService.id}`,
      }).then(() => {
        carService = undefined;
      });
    }
  });

  it('CarServices menu should load CarServices page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('car-service');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CarService').should('exist');
    cy.url().should('match', carServicePageUrlPattern);
  });

  describe('CarService page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(carServicePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CarService page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/car-service/new$'));
        cy.getEntityCreateUpdateHeading('CarService');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carServicePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/car-services',
          body: carServiceSample,
        }).then(({ body }) => {
          carService = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/car-services+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/car-services?page=0&size=20>; rel="last",<http://localhost/api/car-services?page=0&size=20>; rel="first"',
              },
              body: [carService],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(carServicePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CarService page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('carService');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carServicePageUrlPattern);
      });

      it('edit button click should load edit CarService page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CarService');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carServicePageUrlPattern);
      });

      it('edit button click should load edit CarService page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CarService');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carServicePageUrlPattern);
      });

      it('last delete button click should delete instance of CarService', () => {
        cy.intercept('GET', '/api/car-services/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('carService').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carServicePageUrlPattern);

        carService = undefined;
      });
    });
  });

  describe('new CarService page', () => {
    beforeEach(() => {
      cy.visit(`${carServicePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CarService');
    });

    it('should create an instance of CarService', () => {
      cy.get(`[data-cy="name"]`).type('chorus empty');
      cy.get(`[data-cy="name"]`).should('have.value', 'chorus empty');

      cy.get(`[data-cy="address"]`).type('physically limited');
      cy.get(`[data-cy="address"]`).should('have.value', 'physically limited');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        carService = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', carServicePageUrlPattern);
    });
  });
});
