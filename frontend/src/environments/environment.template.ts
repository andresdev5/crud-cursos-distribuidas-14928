export const environment = {
    api: {
        urls: {
            serviceUsers: '${USERS_SERVICE_ENDPOINT:-http://localhost:8001}',
            serviceCourses: '${COURSES_SERVICE_ENDPOINT:-http://localhost:8002}',
        }
    },
};
