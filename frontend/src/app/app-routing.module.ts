import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ResultPageComponent } from './result-page/result-page.component';
import { GermplasmCardComponent } from './germplasm-card/germplasm-card.component';
import { StudyCardComponent } from './study-card/study-card.component';
import { SiteCardComponent } from './site-card/site-card.component';
import { MarkdownPageComponent } from "./markdown-page/markdown-page.component";
import { environment } from "../environments/environment";

export const routes: Routes = [
    { path: 'studies/:id', component: StudyCardComponent },
    { path: 'sites/:id', component: SiteCardComponent },
    { path: '', component: ResultPageComponent },
    { path: 'germplasm', component: GermplasmCardComponent },
    { path: 'about', component: MarkdownPageComponent, data: { mdFile: environment.aboutUsMdFile } },
    { path: 'join', component: MarkdownPageComponent, data: { mdFile: environment.joinUsMdFile } },
    { path: 'legal', component: MarkdownPageComponent, data: { mdFile: environment.legalMentionsMdFile } },
    { path: 'help', component: MarkdownPageComponent, data: { mdFile: environment.helpMdFile } },

];

@NgModule({
    imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
