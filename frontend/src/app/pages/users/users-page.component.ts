import { Component, OnInit } from '@angular/core';
import { UserModel } from '@app/models';
import { ToastService, UserService } from '@app/services';

import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';
import { MenuModule } from 'primeng/menu';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { lastValueFrom } from 'rxjs';
import { SharedModule } from '@app/shared';

interface UserTableItem {
    data: UserModel;
    actions: MenuItem[];
    showPassword?: boolean;
}

@Component({
    standalone: true,
    templateUrl: './users-page.component.html',
    imports: [
        SharedModule,
        TableModule,
        ButtonModule,
        MenuModule,
    ]
})
export class UsersPageComponent implements OnInit {
    public rows: UserTableItem[] = [];

    constructor(
        private confirmationService: ConfirmationService,
        private toastService: ToastService,
        private userService: UserService,
        private router: Router,
    ) {}

    ngOnInit(): void {
        this.loadUsers();
    }

    public async loadUsers(): Promise<void> {
        try {
            const users = await lastValueFrom(this.userService.getAll(), { defaultValue: [] });

            this.rows = users.map((user) => ({
                data: user,
                actions: [
                    {
                        label: 'Editar',
                        icon: 'pi pi-pencil',
                        command: () => this.goToUserForm(user),
                    },
                    {
                        label: 'Eliminar',
                        icon: 'pi pi-trash',
                        command: () => this.deleteUser(user),
                    },
                ],
                showPassword: false,
            }));
        } catch (error) {
            this.toastService.showError('Error', 'Error al cargar los usuarios');
        }
    }

    public goToUserForm(user?: UserModel): void {
        if (user) {
            this.router.navigate(['/users/edit', user.id]);
        } else {
            this.router.navigate(['/users/add']);
        }
    }

    public async deleteUser(user: UserModel) {
        this.confirmationService.confirm({
            message: `Estas seguro que deseas eliminar el usuario '${user.name}'?`,
            header: 'Confirmación',
            icon: 'pi pi-info-circle',
            acceptIcon:"none",
            rejectIcon:"none",
            rejectButtonStyleClass:"p-button-text",
            accept: async () => {
                try {
                    await lastValueFrom(this.userService.delete(user.id));
                    this.toastService.showSuccess('Atención', 'El usuario fue eliminado correctamente');
                    await this.loadUsers();
                } catch (error) {
                    this.toastService.showError('Error', 'Error al eliminar el usuario');
                }
            },
            reject: () => {}
        });
    }
}
