import { Component, Input, OnInit } from '@angular/core';
import {
    DataDiscoveryDocument,
    DataDiscoverySource,
    DataDiscoveryType
} from '../../models/data-discovery.model';

@Component({
    selector: 'faidare-document',
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

    get dataSource(): DataDiscoverySource {
        return this.document['schema:includedInDataCatalog'];
    }

    getURL() {
        return this.document['schema:url'] || '';
    }

    getRouterLink() {
        for (const type of this.document['@type']) {
            const cardUrl = DocumentComponent.CARD_TYPE[type];
            if (cardUrl === 'studies') {
                return `/${cardUrl}/${this.document['schema:identifier']}`;
            }
            if (cardUrl === 'germplasm') {
                return `/${cardUrl}`;
            }
        }

        return '';
    }

    getQueryParam() {
        if (this.document['schema:identifier']) {
            const id: string = this.document['schema:identifier'];
            // TODO: remove condition when schema:identifier will store an encoded
            return { id: btoa(id) };
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
