import { Component } from '@angular/core';
import { environment } from '../../environments/environment';

@Component({
    selector: 'faidare-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {

    navbar = environment.navbar;
    navbarCollapsed = true;

    toggleNavbar() {
        this.navbarCollapsed = !this.navbarCollapsed;
    }
}
