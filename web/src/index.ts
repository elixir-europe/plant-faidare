import { initializePopovers } from './bootstrap/popovers';
import { initializeMap } from './map/map';
import 'bootstrap/js/dist/dropdown';
import 'bootstrap/js/dist/collapse';

(window as any).faidare = {
    initializePopovers,
    initializeMap
};
