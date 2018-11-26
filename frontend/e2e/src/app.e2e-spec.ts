import { AppPage } from './app.po';

describe('workspace-project App', () => {
    let page: AppPage;

    beforeEach(() => {
        page = new AppPage();
    });

    it('should display welcome message', () => {
        page.navigateTo();
        expect(page.getTitleText()).toEqual('home works! Go to results');
    });

    it('should display result page message', () => {
        page.navigateTo('/results');
        expect(page.getTitleText()).toEqual('result works!');
    });

    it('should display site card message', () => {
        page.navigateTo('/sites/FOO');
        expect(page.getTitleText()).toEqual('site-card works!');
    });

    it('should display study card message', () => {
        page.navigateTo('/studies/BAR');
        expect(page.getTitleText()).toEqual('study-card works!');
    });

    it('should display germplasm card message', () => {
        page.navigateTo('/germplasm/BAZ');
        expect(page.getTitleText()).toEqual('germplasm-card works!');
    });
});
