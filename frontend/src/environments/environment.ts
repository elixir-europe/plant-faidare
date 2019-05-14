// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
    production: false,
    navbar: {
        title: 'FAIDARE : FAIR Data-finder for Agronomic REsearch',
        links: [
            {
                label: 'URGI',
                url: '#',
                subMenu: [
                    { label: 'Home', url: 'https://urgi.versailles.inra.fr' },
                    { label: 'News', url: 'https://urgi.versailles.inra.fr/About-us/News' },
                    { label: 'About us', url: 'https://urgi.versailles.inra.fr/About-us' }
                ]
            },
            { label: 'GNPIS', url: 'https://urgi.versailles.inra.fr/gnpis/' },
            { label: 'CIRAD', url: 'http://tropgenedb.cirad.fr/' },
            { label: 'VIB', url: 'http://pippa.psb.ugent.be' },
            { label: 'IBET', url: 'https://biodata.pt' }

        ]
    }
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
