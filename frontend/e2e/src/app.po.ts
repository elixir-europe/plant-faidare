import { browser, by, element } from 'protractor';

export class AppPage {
    navigateTo(path = '/') {
        return browser.get(path);
    }

    getTitleText() {
        return element(by.css('faidare-root p')).getText();
    }

    getLabel(id) {
        return element(by.css('faidare-root label[for=' + id + ']')).getText();
    }
}
