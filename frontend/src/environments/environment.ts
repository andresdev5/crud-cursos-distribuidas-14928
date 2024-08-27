export const environment = {
    api: {
        urls: {
            serviceUsers: 'http://localhost:8000/users-service',
            serviceCourses: 'http://localhost:8000/courses-service',
        }
    },
    keycloak: {
        config: {
            url: 'http://localhost:8003/auth',
            realm: 'SpringBootKeycloak',
            clientId: 'courses-app',
        }
    }
};
