import { Component } from '@angular/core';

@Component({
  selector: 'gpds-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {

  navbar = {
    title: 'GnpIS Plant Data Search',
    links: [{label: 'URGI', url: 'http://urgi.versailles.inra.fr'}]
  };
  navbarCollapsed = true;

  toggleNavbar() {
    this.navbarCollapsed = !this.navbarCollapsed;
  }
}
