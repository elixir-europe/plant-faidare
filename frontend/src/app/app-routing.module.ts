import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ResultPageComponent } from './result-page/result-page.component';
import { GermplasmCardComponent } from './germplasm-card/germplasm-card.component';
import { StudyCardComponent } from './study-card/study-card.component';
import { SiteCardComponent } from './site-card/site-card.component';

const routes: Routes = [
    { path: 'home', component: HomeComponent },
    { path: 'results', component: ResultPageComponent },
    { path: 'germplasm/:id', component: GermplasmCardComponent },
    { path: 'studies/:id', component: StudyCardComponent },
    { path: 'sites/:id', component: SiteCardComponent },
    { path: '', component: HomeComponent },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
