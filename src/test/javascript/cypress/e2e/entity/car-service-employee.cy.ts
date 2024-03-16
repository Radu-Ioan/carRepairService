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

describe('CarServiceEmployee e2e test', () => {
  const carServiceEmployeePageUrl = '/car-service-employee';
  const carServiceEmployeePageUrlPattern = new RegExp('/car-service-employee(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const carServiceEmployeeSample = { name: 'vaguely famously' };

  let carServiceEmployee;
  let carService;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/car-services',
      body: { name: 'harsh', address: 'bah' },
    }).then(({ body }) => {
      carService = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/car-service-employees+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/car-service-employees').as('postEntityRequest');
    cy.intercept('DELETE', '/api/car-service-employees/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/car-services', {
      statusCode: 200,
      body: [carService],
    });
  });

  afterEach(() => {
    if (carServiceEmployee) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/car-service-employees/${carServiceEmployee.id}`,
      }).then(() => {
        carServiceEmployee = undefined;
      });
    }
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

  it('CarServiceEmployees menu should load CarServiceEmployees page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('car-service-employee');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CarServiceEmployee').should('exist');
    cy.url().should('match', carServiceEmployeePageUrlPattern);
  });

  describe('CarServiceEmployee page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(carServiceEmployeePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CarServiceEmployee page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/car-service-employee/new$'));
        cy.getEntityCreateUpdateHeading('CarServiceEmployee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carServiceEmployeePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/car-service-employees',
          body: {
            ...carServiceEmployeeSample,
            carService: carService,
          },
        }).then(({ body }) => {
          carServiceEmployee = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/car-service-employees+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/car-service-employees?page=0&size=20>; rel="last",<http://localhost/api/car-service-employees?page=0&size=20>; rel="first"',
              },
              body: [carServiceEmployee],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(carServiceEmployeePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CarServiceEmployee page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('carServiceEmployee');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carServiceEmployeePageUrlPattern);
      });

      it('edit button click should load edit CarServiceEmployee page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CarServiceEmployee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carServiceEmployeePageUrlPattern);
      });

      it('edit button click should load edit CarServiceEmployee page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CarServiceEmployee');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carServiceEmployeePageUrlPattern);
      });

      it('last delete button click should delete instance of CarServiceEmployee', () => {
        cy.intercept('GET', '/api/car-service-employees/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('carServiceEmployee').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carServiceEmployeePageUrlPattern);

        carServiceEmployee = undefined;
      });
    });
  });

  describe('new CarServiceEmployee page', () => {
    beforeEach(() => {
      cy.visit(`${carServiceEmployeePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CarServiceEmployee');
    });

    it('should create an instance of CarServiceEmployee', () => {
      cy.get(`[data-cy="name"]`).type('um finally spangle');
      cy.get(`[data-cy="name"]`).should('have.value', 'um finally spangle');

      cy.get(`[data-cy="age"]`).type('48');
      cy.get(`[data-cy="age"]`).should('have.value', '48');

      cy.get(`[data-cy="yearsOfExperience"]`).type('3109');
      cy.get(`[data-cy="yearsOfExperience"]`).should('have.value', '3109');

      cy.get(`[data-cy="carService"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        carServiceEmployee = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', carServiceEmployeePageUrlPattern);
    });
  });
});
