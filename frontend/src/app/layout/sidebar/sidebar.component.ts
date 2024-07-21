import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { SharedModule } from '@app/shared/shared.module';

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    standalone: true,
    imports: [
        SharedModule,
        RouterModule
    ]
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
    private router: Router;

    constructor(router: Router) {
        this.router = router;
    }

    ngOnInit(): void {}
}
