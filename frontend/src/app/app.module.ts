import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormComponent } from './form/form.component';
import { ResultPageComponent } from './result-page/result-page.component';
import { GermplasmCardComponent } from './germplasm-card/germplasm-card.component';
import { StudyCardComponent } from './study-card/study-card.component';
import { SiteCardComponent } from './site-card/site-card.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NavbarComponent } from './navbar/navbar.component';
import { MapComponent } from './map/map.component';
import { NgbAlertModule, NgbDropdownModule, NgbPaginationModule, NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SuggestionFieldComponent } from './form/suggestion-field/suggestion-field.component';
import { DocumentComponent } from './result-page/document/document.component';
import { ErrorComponent } from './error/error.component';
import { ErrorInterceptorService } from './error-interceptor.service';
import { TraitOntologyWidgetComponent } from './form/trait-ontology-widget/trait-ontology-widget.component';
import { FacetsComponent } from './result-page/facets/facets.component';


@NgModule({
    declarations: [
        AppComponent,
        FormComponent,
        ResultPageComponent,
        GermplasmCardComponent,
        StudyCardComponent,
        SiteCardComponent,
        NavbarComponent,
        MapComponent,
        SuggestionFieldComponent,
        DocumentComponent,
        ErrorComponent,
        TraitOntologyWidgetComponent,
        FacetsComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        NgbTypeaheadModule,
        NgbPaginationModule,
        NgbAlertModule,
        NgbDropdownModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,

    ],
    providers: [
        { provide: HTTP_INTERCEPTORS, useExisting: ErrorInterceptorService, multi: true }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
