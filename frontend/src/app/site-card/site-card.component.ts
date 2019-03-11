import { Component, OnInit } from '@angular/core';
import { BrapiService } from '../brapi.service';
import { ActivatedRoute } from '@angular/router';
import { BrapiLocation } from '../models/brapi.model';
import { KeyValueObject } from '../utils';

@Component({
    selector: 'gpds-site-card',
    templateUrl: './site-card.component.html',
    styleUrls: ['./site-card.component.scss']
})
export class SiteCardComponent implements OnInit {

    location: BrapiLocation;
    additionalInfos: KeyValueObject[];
    loading = true;


    constructor(private brapiService: BrapiService, private route: ActivatedRoute) {
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
                        console.log(this.location.additionalInfo);
                        this.manageAdditionalInfo(KeyValueObject.fromObject(this.location.additionalInfo));
                    }
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

    formatCoordinates(decimalDegrees: number, type: string): string {
        if (decimalDegrees && type) {
            const degree = Math.floor(decimalDegrees);
            const decimalMinute = (decimalDegrees - degree) * 60;
            const minute = Math.floor(decimalMinute);
            const decimalSeconde = (decimalMinute - minute) * 60;
            const seconde = Math.floor(decimalSeconde);

            let direction = '';
            if (type === 'latitude') {
                if (decimalDegrees >= 0) {
                    direction = 'N';
                } else {
                    direction = 'S';
                }
            } else if (type === 'longitude') {
                if (decimalDegrees >= 0) {
                    direction = 'E';
                } else {
                    direction = 'W';
                }
            }
            return decimalDegrees + '° (' + degree + '° ' + minute + '\' ' + seconde + '\'\' ' + direction + ')';
        } else {
            return null;
        }
    }

}

