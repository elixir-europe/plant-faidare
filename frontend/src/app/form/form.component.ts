import { Component, Input } from '@angular/core';
import { DataDiscoveryCriteria } from '../model/dataDiscoveryCriteria';
import { BehaviorSubject } from 'rxjs';

@Component({
    selector: 'gpds-form',
    templateUrl: './form.component.html',
    styleUrls: ['./form.component.scss']
})
export class FormComponent {
    @Input() criteria$: BehaviorSubject<DataDiscoveryCriteria>;
}
