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

    get providerLinks() {
        return this.links[1].elements('a');
    }

    get logos() {
        return this.elements('img');
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

        expect(tester.logos.length).toBe(2);
        expect(tester.logos.shift().attr('title')).toBe('FAIR Data-finder for Agronomic REsearch');
        expect(tester.logos.pop().attr('title')).toBe('Elixir');

        expect(tester.links.length).toBe(3);
        // 'More...' section (containing Help, About, Join and Legal links) added automatically
        // 'Data providers' section (containing data sources) fetch and added automatically

        expect(tester.firstLink.textContent).toBe('URGI');
        expect(tester.firstLink.attr('href')).toBe('https://urgi.versailles.inra.fr/');
        expect(tester.firstLink.attr('target')).toBe('_blank');

        expect(tester.providerLinks.length - 1).toBe(2);
        // minus 1 because of the dropdown link
        expect(tester.providerLinks.shift().textContent).toBe('Data providers');
        expect(tester.providerLinks.pop().textContent).toBe('Example source2');
        expect(tester.providerLinks.pop().attr('href')).toBe('http://example2.com');
        expect(tester.providerLinks.pop().attr('target')).toBe('_blank');

        expect(tester.links[2].element('a').textContent).toBe('More...');

    }));

})

;
