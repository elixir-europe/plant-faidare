import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'faidare-markdown',
    templateUrl: './markdown-page.component.html',
    styleUrls: ['./markdown-page.component.scss']
})
export class MarkdownPageComponent implements OnInit {

    mdFile = '';

    constructor(private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.route.data.subscribe(
            value => this.mdFile = value.mdFile);
    }

    onLoad(e: any) {
        // console.log('Into onLoad');
        // console.log(e);
    }

    onError(e: any) {
        console.log('Got error', e);
    }

}
