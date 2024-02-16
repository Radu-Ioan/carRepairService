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

describe('CarRepairAppointment e2e test', () => {
  const carRepairAppointmentPageUrl = '/car-repair-appointment';
  const carRepairAppointmentPageUrlPattern = new RegExp('/car-repair-appointment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const carRepairAppointmentSample = { date: '2024-02-13' };

  let carRepairAppointment;
  let car;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/cars',
      body: { company: 'which', manufacturedYear: 31338, ownerName: 'now' },
    }).then(({ body }) => {
      car = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/car-repair-appointments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/car-repair-appointments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/car-repair-appointments/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/cars', {
      statusCode: 200,
      body: [car],
    });
  });

  afterEach(() => {
    if (carRepairAppointment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/car-repair-appointments/${carRepairAppointment.id}`,
      }).then(() => {
        carRepairAppointment = undefined;
      });
    }
  });

  afterEach(() => {
    if (car) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cars/${car.id}`,
      }).then(() => {
        car = undefined;
      });
    }
  });

  it('CarRepairAppointments menu should load CarRepairAppointments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('car-repair-appointment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CarRepairAppointment').should('exist');
    cy.url().should('match', carRepairAppointmentPageUrlPattern);
  });

  describe('CarRepairAppointment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(carRepairAppointmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CarRepairAppointment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/car-repair-appointment/new$'));
        cy.getEntityCreateUpdateHeading('CarRepairAppointment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carRepairAppointmentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/car-repair-appointments',
          body: {
            ...carRepairAppointmentSample,
            car: car,
          },
        }).then(({ body }) => {
          carRepairAppointment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/car-repair-appointments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/car-repair-appointments?page=0&size=20>; rel="last",<http://localhost/api/car-repair-appointments?page=0&size=20>; rel="first"',
              },
              body: [carRepairAppointment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(carRepairAppointmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CarRepairAppointment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('carRepairAppointment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carRepairAppointmentPageUrlPattern);
      });

      it('edit button click should load edit CarRepairAppointment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CarRepairAppointment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carRepairAppointmentPageUrlPattern);
      });

      it('edit button click should load edit CarRepairAppointment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CarRepairAppointment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carRepairAppointmentPageUrlPattern);
      });

      it('last delete button click should delete instance of CarRepairAppointment', () => {
        cy.intercept('GET', '/api/car-repair-appointments/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('carRepairAppointment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', carRepairAppointmentPageUrlPattern);

        carRepairAppointment = undefined;
      });
    });
  });

  describe('new CarRepairAppointment page', () => {
    beforeEach(() => {
      cy.visit(`${carRepairAppointmentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CarRepairAppointment');
    });

    it('should create an instance of CarRepairAppointment', () => {
      cy.get(`[data-cy="date"]`).type('2024-02-14');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2024-02-14');

      cy.get(`[data-cy="car"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        carRepairAppointment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', carRepairAppointmentPageUrlPattern);
    });
  });
});
