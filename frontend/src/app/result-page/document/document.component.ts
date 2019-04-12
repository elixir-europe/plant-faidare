import { Component, Input, OnInit } from '@angular/core';
import { DataDiscoveryDocument, DataDiscoverySource, DataDiscoveryType } from '../../models/data-discovery.model';
import { GnpisService } from '../../gnpis.service';

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
        // TODO: index URGI schema:identifier like the partners
        const urgiStudy = this.dataSource['schema:url'] === GnpisService.URGI_SOURCE_URI;
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
