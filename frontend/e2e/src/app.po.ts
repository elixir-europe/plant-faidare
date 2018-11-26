import { browser, by, element } from 'protractor';

export class AppPage {
    navigateTo(path = '/') {
        return browser.get(path);
    }

    getTitleText() {
        return element(by.css('gpds-root p')).getText();
    }
}
