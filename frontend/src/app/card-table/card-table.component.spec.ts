import { async, TestBed } from '@angular/core/testing';

import { CardTableComponent } from './card-table.component';
import { Component, ViewChild } from '@angular/core';

/**
 * Test gpds-card-table with a simple provided row `ng-template`
 */
@Component({
    selector: 'gpds-test',
    template: `
        <gpds-card-table>
            <ng-template let-row>
                <tr>
                    <td>{{ row[0] }}</td>
                    <td>{{ row[1] }}</td>
                    <td>{{ row[2] }}</td>
                </tr>
            </ng-template>
        </gpds-card-table>`
})
class CardTableTestWrapperComponent {
    @ViewChild(CardTableComponent) component: CardTableComponent;
}

describe('CardTableComponent', () => {

    beforeEach(async(() =>
        TestBed.configureTestingModule({
            declarations: [CardTableComponent, CardTableTestWrapperComponent]
        }).compileComponents()
    ));

    const headers = [
        'h1', 'h2', 'h3'
    ];
    const rows = [
        ['a', 'b', 'c'],
        ['d', 'e', 'f'],
        ['g', 'h', 'i'],
    ];

    it('should hide headers and show rows', () => {
        const fixture = TestBed.createComponent(CardTableTestWrapperComponent);
        const component = fixture.componentInstance.component;
        component.headers = null;
        component.rows = rows;
        fixture.detectChanges();

        const element: HTMLElement = fixture.nativeElement;
        const thead = element.querySelector('thead');
        expect(thead).toBeFalsy();

        const tds = element.querySelectorAll('td');
        expect(tds.length).toBe(9);
        expect(tds[0].textContent).toContain(rows[0][0]);
        expect(tds[5].textContent).toContain(rows[1][2]);
    });

    it('should show headers and rows', () => {
        const fixture = TestBed.createComponent(CardTableTestWrapperComponent);
        const component = fixture.componentInstance.component;
        component.headers = headers;
        component.rows = rows;
        fixture.detectChanges();

        const element: HTMLElement = fixture.nativeElement;
        const ths = element.querySelectorAll('thead th');
        expect(ths.length).toBe(3);
        expect(ths[0].textContent).toContain(headers[0]);
        expect(ths[2].textContent).toContain(headers[2]);

        const tds = element.querySelectorAll('td');
        expect(tds.length).toBe(9);
        expect(tds[0].textContent).toContain(rows[0][0]);
        expect(tds[5].textContent).toContain(rows[1][2]);
    });
});
