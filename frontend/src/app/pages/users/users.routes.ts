import { inject } from '@angular/core';
import { Routes } from '@angular/router';
import { UserModel } from '@app/models';
import { UserService } from '@app/services';
import { Observable } from 'rxjs';

export const routes: Routes = [
    {
        path: '',
        title: 'Usuarios',
        loadComponent: async () => (await import('./users-page.component')).UsersPageComponent,
    },
    {
        path: 'add',
        title: 'Agregar usuario',
        loadComponent: async () => (await import('./components/user-form/user-form.component')).UserFormComponent,
    },
    {
        path: 'edit/:id',
        title: 'Editar usuario',
        loadComponent: async () => (await import('./components/user-form/user-form.component')).UserFormComponent,
        resolve: {
            user: (route: any, state: any): Observable<UserModel> => {
                const id = route.paramMap.get('id');
                return inject(UserService).getById(id);
            }
        }
    }
]
