import { Popover } from 'bootstrap';

export function initializePopovers() {
    const popoverTriggerList: Array<HTMLElement> = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
    popoverTriggerList.forEach(popoverTriggerEl => {
        const options: Partial<Popover.Options> = {};
        const contentSelector = popoverTriggerEl.dataset.bsElement;
        if (contentSelector) {
            const content = document.querySelector(contentSelector);
            if (content) {
                options.content = content.innerHTML;
                options.html = true;
            } else {
                throw new Error('element with selector ' + contentSelector + ' not found');
            }
        }
        return new Popover(popoverTriggerEl, options);
    });
}
