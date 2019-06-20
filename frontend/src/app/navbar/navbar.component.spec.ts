import { async, TestBed } from '@angular/core/testing';

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
        return this.elements('li a');
    }

    get logo() {
        return this.element('img');
    }
}

describe('NavbarComponent', () => {

    const dataSource1: DataDiscoverySource = {
        '@id': 'urn:source1',
        '@type': ['schema:DataCatalog'],
        'schema:name': 'Example source1',
        'schema:url': 'http://example1.com',
        'schema:image': 'http://example1.com/logo.png'
    };

    const dataSource2: DataDiscoverySource = {
        '@id': 'urn:source2',
        '@type': ['schema:DataCatalog'],
        'schema:name': 'Example source2',
        'schema:url': 'http://example2.com',
        'schema:image': 'http://example2.com/logo.png'
    };

    let gnpisService;

    beforeEach(() => {
        gnpisService = jasmine.createSpyObj(
            'GnpisService', [
                'getSource',
                'suggest'
            ]
        );
        gnpisService.suggest.and.returnValue(of([dataSource1['@id'], dataSource2['@id']]));
        gnpisService.getSource.withArgs(dataSource1['@id']).and.returnValue(of(dataSource1));
        gnpisService.getSource.withArgs(dataSource2['@id']).and.returnValue(of(dataSource2));

        TestBed.configureTestingModule({
            declarations: [NavbarComponent],
            providers: [
                { provide: GnpisService, useValue: gnpisService }
            ]
        }).compileComponents();
    });

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
                { label: 'INRA', url: 'http://www.inra.fr/' },
                { label: 'URGI', url: 'https://urgi.versailles.inra.fr/' }
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

        // Two static links + two dynamic links (fetched data sources)
        expect(tester.links.length).toBe(4);

        expect(tester.links[0].textContent).toBe('INRA');
        expect(tester.links[0].attr('href')).toBe('http://www.inra.fr/');
        expect(tester.links[0].attr('target')).toBe('_blank');
    }));
})
;
