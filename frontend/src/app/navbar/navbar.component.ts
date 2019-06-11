import { Component } from '@angular/core';
import { environment } from '../../environments/environment';
import { routes as appUrlRoutes } from '../app-routing.module';
import { ActivatedRoute, Params, Router, RoutesRecognized } from '@angular/router';

@Component({
    selector: 'faidare-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {

    displayBackButton = false;
    previousQueryParam: Params;

    constructor(private router: Router, private route: ActivatedRoute
    ) {

        this.router.events.subscribe(event => {
            if (event instanceof RoutesRecognized) {
                if (this.checkIsFormPage(event.url)) {
                    this.displayBackButton = false;
                    this.route.queryParams.subscribe(queryParam => {
                        this.previousQueryParam = queryParam;
                    });
                } else {
                    this.displayBackButton = true;
                }
            }
        });
    }

    navbar = environment.navbar;
    navbarCollapsed = true;

    toggleNavbar() {
        this.navbarCollapsed = !this.navbarCollapsed;
    }

    checkIsFormPage(url: String) {
        for (const route of appUrlRoutes) {
            const routeType: string = route.path.split(':')[0];
            if (routeType !== '' && url.startsWith('/' + routeType)) {
                return false;
            }
        }
        return true;
    }
}
