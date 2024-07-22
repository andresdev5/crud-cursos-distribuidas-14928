import { defineConfig } from 'cypress'

export default defineConfig({
    e2e: {
        'baseUrl': 'http://localhost:4200',
        'port': 4200
    },


    component: {
        devServer: {
            framework: 'angular',
            bundler: 'webpack',
        },
        specPattern: 'tests/e2e/**/*.cy.ts'
    }
})
