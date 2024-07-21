import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: 'users',
        loadChildren: async () => (await import('@app/pages/users')).routes,
    },
    {
        path: 'courses',
        loadChildren: async () => (await import('@app/pages/courses')).routes,
    },
    {
        path: 'enrollments',
        loadChildren: async () => (await import('@app/pages/enrollments')).routes,
    },
    {
        path: '',
        loadChildren: async () => (await import('@app/pages/users')).routes,
    }
];
