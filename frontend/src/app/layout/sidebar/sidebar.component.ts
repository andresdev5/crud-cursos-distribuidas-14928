import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { AuthService } from '@app/services/auth.service';
import { SharedModule } from '@app/shared/shared.module';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    standalone: true,
    imports: [
        SharedModule,
        RouterModule
    ],
})
export class SidebarComponent implements OnInit {
    public routes: any[] = [
        {
            title: 'Usuarios',
            icon: 'users',
            link: ['/users'],
            active: false,
        },
        {
            title: 'Cursos',
            icon: 'book',
            link: ['/courses'],
            active: false,
        },
        {
            title: 'Matrículas',
            icon: 'user-plus',
            link: ['/enrollments'],
            active: false,
        }
    ];
    public isLogged: boolean = false;
    public userProfile: KeycloakProfile | undefined;

    constructor(private router: Router, private authService: AuthService) {
        this.router = router;
        this.authService = authService;
    }

    logout() {
        this.authService.logout();
    }

    ngOnInit(): void {
        this.isLogged = this.authService.isLoggedIn();

        if (this.isLogged) {
            this.authService.userProfile().subscribe((profile) => {
                this.userProfile = profile;
            });
        }
    }
}
