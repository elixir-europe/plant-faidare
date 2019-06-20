import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import { BrapiLocation } from '../models/brapi.model';
import { KeyValueObject, toKeyValueObjects } from '../utils';
import { DataDiscoverySource } from '../models/data-discovery.model';
import { GnpisService } from '../gnpis.service';
import { Direction, TransformationType } from 'angular-coordinates';

@Component({
    selector: 'faidare-site-card',
    templateUrl: './site-card.component.html',
    styleUrls: ['./site-card.component.scss']
})
export class SiteCardComponent implements OnInit {

    location: BrapiLocation;
    locationSource: DataDiscoverySource;
    additionalInfos: KeyValueObject[];
    direction = Direction;
    type = TransformationType;
    loading = true;

    constructor(private brapiService: BrapiService, private gnpisService: GnpisService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.route.paramMap.subscribe(paramMap => {
            // initialize site from location ID
            const locationId = paramMap.get('id');
            this.brapiService.location(locationId).subscribe(
                response => {
                    this.location = response.result;
                    this.additionalInfos = [];
                    if (this.location.additionalInfo) {
                        this.manageAdditionalInfo(toKeyValueObjects(this.location.additionalInfo).sort());
                    }
                    this.locationSource = location['schema:includedInDataCatalog'];
                    this.loading = false;
                }
            );
        });

    }

    manageAdditionalInfo(keyValues: KeyValueObject[]) {
        const forbiddenElements: String[] = [
            'Site status',
            'Coordinates precision',
            'Slope',
            'Exposure',
            'Geographical location',
            'Distance to city',
            'Direction from city',
            'Environment type',
            'Topography',
            'Comment'];
        for (const keyValue of keyValues) {
            if (!forbiddenElements.includes(keyValue.key)) {
                this.additionalInfos.push(keyValue);
            }
        }
    }
}

