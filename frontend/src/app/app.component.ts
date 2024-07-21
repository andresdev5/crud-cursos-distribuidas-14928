import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SharedModule } from '@app/shared/shared.module';
import { SidebarComponent } from '@app/layout/sidebar/sidebar.component';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';

import { ToastModule } from 'primeng/toast';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToastService } from './services';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [
        SharedModule,
        RouterOutlet,
        SidebarComponent,
        LoadingBarRouterModule,
        ToastModule,
        ConfirmDialogModule,
        DialogModule,
    ],
    templateUrl: './app.component.html',
    providers: [ConfirmationService, MessageService, ToastService]
})
export class AppComponent {}
