import { Component, Input, OnInit } from '@angular/core';
import { GnpisService } from '../gnpis.service';
import { XrefModel } from '../models/xref.model';

@Component({
    selector: 'faidare-xrefs',
    templateUrl: './xrefs.component.html',
    styleUrls: ['./xrefs.component.scss']
})
export class XrefsComponent implements OnInit {

    xrefs: Array<XrefModel> = new Array<XrefModel>();
    @Input() xrefId: string;

    constructor(private gnpisService: GnpisService) {
    }

    ngOnInit() {
        this.gnpisService.xref(this.xrefId).subscribe(
            xrefs => {
                this.xrefs = xrefs;
            }
        );
    }
}
