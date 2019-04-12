import { Component, Input, OnInit } from '@angular/core';
import * as schema from '../models/schema.org.model';
import { GnpisService } from '../gnpis.service';

@Component({
    selector: 'gpds-card-generic-document',
    templateUrl: './card-generic-document.component.html'
})
export class CardGenericDocumentComponent implements OnInit {

    @Input() document: schema.Dataset;
    @Input() documentType: string;

    dataSource: schema.DataCatalog;

    constructor(private gnpisService: GnpisService) {
    }

    ngOnInit() {
        let sourceURI = this.document['schema:includedInDataCatalog'];

        // TODO: Make sure this never happen in ETL (always fill this field)
        if (typeof sourceURI !== 'string') {
            sourceURI = GnpisService.URGI_SOURCE_URI;
        }

        this.gnpisService.getSource(sourceURI).subscribe(dataSource => {
            this.dataSource = dataSource;
        });
    }

}
