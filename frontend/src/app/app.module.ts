import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormComponent } from './form/form.component';
import { ResultPageComponent } from './result-page/result-page.component';
import { GermplasmCardComponent } from './germplasm-card/germplasm-card.component';
import { GermplasmResultPageComponent } from './germplasm-result-page/germplasm-result-page.component';
import { StudyCardComponent } from './study-card/study-card.component';
import { SiteCardComponent } from './site-card/site-card.component';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { NavbarComponent } from './navbar/navbar.component';
import { MapComponent } from './map/map.component';
import { NgbAlertModule, NgbDropdownModule, NgbPaginationModule, NgbPopoverModule, NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SuggestionFieldComponent } from './form/suggestion-field/suggestion-field.component';
import { DocumentComponent } from './result-page/document/document.component';
import { ErrorComponent } from './error/error.component';
import { ErrorInterceptorService } from './error-interceptor.service';
import { TraitOntologyWidgetComponent } from './form/trait-ontology-widget/trait-ontology-widget.component';
import { FacetsComponent } from './result-page/facets/facets.component';
import { CardRowComponent } from './card-row/card-row.component';
import { CardSectionComponent } from './card-section/card-section.component';
import { LoadingSpinnerComponent } from './loading-spinner/loading-spinner.component';
import { CardTableComponent } from './card-table/card-table.component';
import { CardSortableTableComponent } from './card-sortable-table/card-sortable-table.component';
import { MomentModule } from 'ngx-moment';
import { XrefsComponent } from './xrefs/xrefs.component';
import { CoordinatesModule } from 'angular-coordinates';
import { CardGenericDocumentComponent } from './card-generic-document/card-generic-document.component';
import { MarkdownModule, MarkedOptions, MarkedRenderer } from 'ngx-markdown';
import { MarkdownPageComponent } from './markdown-page/markdown-page.component';
import { NgbdSortableHeader } from './card-sortable-table/sortable.directive';
import { DecimalPipe } from '@angular/common';

@NgModule({
    declarations: [
        AppComponent,
        FormComponent,
        ResultPageComponent,
        GermplasmCardComponent,
        GermplasmResultPageComponent,
        NgbdSortableHeader,
        StudyCardComponent,
        SiteCardComponent,
        NavbarComponent,
        MapComponent,
        SuggestionFieldComponent,
        DocumentComponent,
        ErrorComponent,
        TraitOntologyWidgetComponent,
        FacetsComponent,
        CardRowComponent,
        CardSectionComponent,
        LoadingSpinnerComponent,
        CardTableComponent,
        CardSortableTableComponent,
        XrefsComponent,
        CardGenericDocumentComponent,
        MarkdownPageComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        NgbTypeaheadModule,
        NgbPaginationModule,
        NgbAlertModule,
        NgbDropdownModule,
        NgbPopoverModule,
        FormsModule,
        ReactiveFormsModule,
        MomentModule,
        CoordinatesModule,
        MarkdownModule.forRoot({
            loader: HttpClient, // optional, only if you use [src] attribute
            markedOptions: {
                provide: MarkedOptions,
                useFactory: markedOptionsFactory,
                useValue: {
                    gfm: true, // default
                    tables: true,
                    breaks: false,
                    pedantic: false,
                    sanitize: false,
                    smartLists: true,
                    smartypants: false
                },
            }
        }),

    ],
    providers: [
        { provide: HTTP_INTERCEPTORS, useExisting: ErrorInterceptorService, multi: true },
        DecimalPipe
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}

export function markedOptionsFactory(): MarkedOptions {

    const renderer = new MarkedRenderer();

    renderer.link = (href: string, title: string, text: string) => {
        if (href.startsWith('#')) {
            const fragment = href.split('#')[1];
            return `<a href='${location.pathname}#${fragment}'>${text}</a>`;
        }
        return `<a href="${href}" target="_blank" >${text}</a>`;
    };
    return {
        renderer: renderer
    };
}
