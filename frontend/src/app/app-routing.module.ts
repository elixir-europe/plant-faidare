import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ResultPageComponent } from './result-page/result-page.component';
import { GermplasmCardComponent } from './germplasm-card/germplasm-card.component';
import { StudyCardComponent } from './study-card/study-card.component';
import { SiteCardComponent } from './site-card/site-card.component';

const routes: Routes = [
    { path: 'germplasm/:id', component: GermplasmCardComponent },
    { path: 'germplasm?id=:id', component: GermplasmCardComponent },
    { path: 'studies/:id', component: StudyCardComponent },
    { path: 'sites/:id', component: SiteCardComponent },
    { path: '', component: ResultPageComponent },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
