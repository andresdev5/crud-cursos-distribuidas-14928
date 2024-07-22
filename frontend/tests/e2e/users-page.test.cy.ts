import { fixturesApi } from '../utils/fixtures';

describe('Users Page', () => {
    beforeEach(() => {
        cy.intercept('GET', 'http://localhost:8001/users', { body: fixturesApi.users.getAll() }).as('getUsers')
    });

    it('Check Users page', () => {
        cy.visit('/users');
        cy.contains('Usuarios');
    });
});
