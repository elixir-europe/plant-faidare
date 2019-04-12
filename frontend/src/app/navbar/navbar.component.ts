import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment';
import { GnpisService } from '../gnpis.service';
import { flatMap } from 'rxjs/operators';
import { of } from 'rxjs/internal/observable/of';

interface Link {
    label: string;
    url: string;
    subMenu?: Link[];
}

interface NavBar {
    title: string;
    links: Link[];
}

@Component({
    selector: 'faidare-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

     /*displayBackButton = false;
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
    }*/

    navbar: NavBar = environment.navbar;
    navbarCollapsed = true;

    constructor(private gnpisService: GnpisService) {
    }

    ngOnInit() {
        // Fetch source URIs for with we have data
        this.gnpisService.suggest('sources')
            .pipe(
                // Flatten observable of array of string into observable of string
                flatMap(dataSourceURIs => of(...dataSourceURIs)),
                // For each source URI get actual source description
                flatMap(dataSourceURI => this.gnpisService.getSource(dataSourceURI)),
            )
            .subscribe(dataSource => {
                // Add each data source to the nav bar links
                this.navbar.links.push({
                    label: dataSource['schema:name'],
                    url: dataSource['schema:url']
                });
            });
    }

    toggleNavbar() {
        this.navbarCollapsed = !this.navbarCollapsed;
    }

    /*checkIsFormPage(url: String) {
        for (const route of appUrlRoutes) {
            const routeType: string = route.path.split(':')[0];
            if (routeType !== '' && url.startsWith('/' + routeType)) {
                return false;
            }
        }
        return true;
    }*/
}
