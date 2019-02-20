import { Component, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GnpisService } from '../gnpis.service';
import { XrefModel } from '../models/xref.model';

@Component({
    selector: 'gpds-xrefs',
    templateUrl: './xrefs.component.html',
    styleUrls: ['./xrefs.component.scss']
})
export class XrefsComponent implements OnInit {


    xrefs: Array<XrefModel> = new Array<XrefModel>();
    @Input() id: number;

    constructor(private gnpisService: GnpisService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.gnpisService.xref(this.id).subscribe(
            xrefs => {
                console.log(xrefs);
                this.xrefs = xrefs;
            }
        );
    }
}
