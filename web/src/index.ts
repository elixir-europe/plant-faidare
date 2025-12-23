import { initializePopovers } from './bootstrap/popovers';
import { initializeMap } from './map/map';
import 'bootstrap/js/dist/dropdown';
import 'bootstrap/js/dist/collapse';
import 'bootstrap/js/dist/modal';
import { initializeTrialExport } from './trial/trial-export';

(window as any).faidare = {
  initializePopovers,
  initializeMap,
  initializeTrialExport
};
