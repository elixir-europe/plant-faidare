import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { FormComponent } from './form/form.component';
import { ResultPageComponent } from './result-page/result-page.component';
import { GermplasmCardComponent } from './germplasm-card/germplasm-card.component';
import { StudyCardComponent } from './study-card/study-card.component';
import { SiteCardComponent } from './site-card/site-card.component';
import { HttpClientModule } from '@angular/common/http';
import { NavbarComponent } from './navbar/navbar.component';
import { MapComponent } from './map/map.component';
import { NgbPaginationModule, NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { ReactiveFormsModule } from '@angular/forms';
import { SuggestionFieldComponent } from './form/suggestion-field/suggestion-field.component';
import { DocumentComponent } from './result-page/document/document.component';

@NgModule({
    declarations: [
        AppComponent,
        HomeComponent,
        FormComponent,
        ResultPageComponent,
        GermplasmCardComponent,
        StudyCardComponent,
        SiteCardComponent,
        NavbarComponent,
        MapComponent,
        SuggestionFieldComponent,
        DocumentComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        NgbTypeaheadModule,
        NgbPaginationModule,
        ReactiveFormsModule,
        HttpClientModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
