import { Component, Input, OnInit } from '@angular/core';
import { DataDiscoveryDocument, DataDiscoveryType } from '../../models/data-discovery.model';

@Component({
    selector: 'gpds-document',
    templateUrl: './document.component.html',
    styleUrls: ['./document.component.scss']
})
export class DocumentComponent implements OnInit {
    private static MAX_LENGTH = 256;
    private static BADGE_TYPE = {
        'Germplasm': 'badge-germplasm',
        'Phenotyping Study': 'badge-study'
    };

    private static CARD_TYPE = {
        'Germplasm': 'germplasm',
        'Phenotyping Study': 'studies'
    };

    @Input() document: DataDiscoveryDocument;
    needTruncation = false;
    opened = false;


    getURL() {
        return this.document['schema:url'] || '';
    }

    // TODO: index URGI schema:identifier like the partners

    getRouterLink() {
        const urgiStudy = this.document['schema:includedInDataCatalog']['schema:url'] === 'https://urgi.versailles.inra.fr/gnpis/';
        for (const type of this.document['@type']) {
            const cardUrl = DocumentComponent.CARD_TYPE[type];
            if (cardUrl === 'studies') {
                if (urgiStudy) {
                    const studyId = this.document['@id'].replace(/urn:URGI\/study\//, '');
                    return `/${cardUrl}/${studyId}`;
                }
                return `/${cardUrl}/${this.document['schema:identifier']}`;
            }
            if (cardUrl === 'germplasm') {
                return `/${cardUrl}`;
            }
        }

        return '';
    }

    getSource() {
        return this.document['schema:includedInDataCatalog']['schema:name'];
    }

    getSourceURL() {
        return this.document['schema:includedInDataCatalog']['schema:url'];
    }

    getQueryParam() {
        if (this.document['schema:identifier']) {
            return {
                id: this.document['schema:identifier']
            };
        } else {
            return {
                pui: this.document['@id']
            };
        }
    }

    getBadgeType(type: DataDiscoveryType) {
        return DocumentComponent.BADGE_TYPE[type];
    }

    ngOnInit(): void {
        this.needTruncation = this.document['schema:description'].length > DocumentComponent.MAX_LENGTH;
    }

    toggleDescription() {
        this.opened = !this.opened;
    }

    getTruncatedDescription() {
        return this.document['schema:description'].slice(0, DocumentComponent.MAX_LENGTH);
    }
}
