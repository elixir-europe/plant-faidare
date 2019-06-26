import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarComponent } from './navbar.component';
import { ComponentTester } from 'ngx-speculoos';
import { DataDiscoverySource } from '../models/data-discovery.model';
import { of } from 'rxjs';
import { GnpisService } from '../gnpis.service';

class NavbarComponentTester extends ComponentTester<NavbarComponent> {
    constructor() {
        super(NavbarComponent);
    }

    get navBar() {
        return this.element('#navbar');
    }

    get toggler() {
        return this.button('button');
    }

    get links() {
        return this.elements('li');
    }

    get firstLink() {
        return this.element('li').element('a');
    }

    get firstLinkSubLinks() {
        return this.element('li').elements('a');
    }

    get logo() {
        return this.element('img');
    }
}

describe('NavbarComponent', () => {

    beforeEach(() => TestBed.configureTestingModule({
        declarations: [NavbarComponent]
    }));

    it('should toggle the class on click', () => {
        const tester = new NavbarComponentTester();

        tester.detectChanges();

        expect(tester.navBar.classes).toContain('collapse');

        tester.toggler.click();

        tester.detectChanges();

        expect(tester.navBar.classes).not.toContain('collapse');
    });

    it('should display title and links that open in new tabs', async(() => {
        const tester = new NavbarComponentTester();
        const component = tester.componentInstance;

        component.navbar = {
            name: 'FAIDARE',
            title: 'FAIR Data-finder for Agronomic REsearch',
            logo: 'assets/applicationLogo.png',
            links: [
                {
                    label: 'Data providers',
                    url: '#',
                    subMenu: [
                        { label: 'GNPIS', url: 'https://urgi.versailles.inra.fr/gnpis/' },
                        { label: 'URGI', url: 'https://urgi.versailles.inra.fr/' }
                    ]
                }
            ],
            contributor: {
                name: 'Elixir',
                url: 'https://elixir-europe.org/',
                logo: 'assets/elixir_logo.png'
            }
        };

        tester.detectChanges();
        expect(gnpisService.suggest).toHaveBeenCalledTimes(1);
        expect(gnpisService.getSource).toHaveBeenCalledTimes(2);

        expect(tester.logo.attr('title')).toBe('FAIR Data-finder for Agronomic REsearch');

        expect(tester.links.length - 1).toBe(1);
        // minus 1 because of More section (containing Help, About, Join and Legal links) added automatically
        // Two static links + two dynamic links (fetched data sources)
        //expect(tester.links.length).toBe(4);

        expect(tester.firstLink.textContent).toBe('Data providers');
        expect(tester.firstLinkSubLinks.length -1).toBe(2);
        // minus 1 because of the dropdown link

        expect(tester.firstLinkSubLinks.pop().attr('href')).toBe('https://urgi.versailles.inra.fr/');
        expect(tester.firstLinkSubLinks.pop().textContent).toBe('URGI');
        expect(tester.firstLinkSubLinks.pop().attr('target')).toBe('_blank');
    });
});
