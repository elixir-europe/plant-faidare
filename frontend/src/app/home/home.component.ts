import { Component, OnInit } from '@angular/core';
import { GnpisService } from '../gnpis.service';

@Component({
  selector: 'gpds-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  crops: Array<string> = [];

  constructor(private gnpisService: GnpisService) { }

  ngOnInit() {
    this.gnpisService.suggest('crops', 10).subscribe(crops => {
      this.crops = crops;
    });
  }

}
