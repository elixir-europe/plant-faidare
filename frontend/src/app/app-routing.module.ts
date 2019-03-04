import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ResultPageComponent } from './result-page/result-page.component';
import { GermplasmCardComponent } from './germplasm-card/germplasm-card.component';
import { StudyCardComponent } from './study-card/study-card.component';
import { SiteCardComponent } from './site-card/site-card.component';

export const routes: Routes = [
    { path: 'studies/:id', component: StudyCardComponent },
    { path: 'sites/:id', component: SiteCardComponent },
    { path: '', component: ResultPageComponent },
    {
        path: 'germplasm',
        component: GermplasmCardComponent,
        runGuardsAndResolvers: 'paramsOrQueryParamsChange'
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
