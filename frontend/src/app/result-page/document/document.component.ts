import { Component, Input, OnInit } from '@angular/core';
import { DataDiscoveryDocument, DataDiscoveryType } from '../../model/data-discovery.model';

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

    getRouterLink() {
        if (!this.getURL()) {
            for (const type of this.document['@type']) {
                const cardUrl = DocumentComponent.CARD_TYPE[type];
                if (cardUrl) {
                    return `/${cardUrl}/${this.document['schema:identifier']}`;
                }
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
