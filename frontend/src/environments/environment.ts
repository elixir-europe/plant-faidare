// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
    production: false,
    navbar: {
        title: 'GnpIS Plant Data Search',
        links: [
            { label: 'INRA', url: 'http://www.inra.fr/' },
            {
                label: 'URGI',
                url: '#',
                subMenu: [
                    { label: 'Home', url: 'https://urgi.versailles.inra.fr' },
                    { label: 'News', url: 'https://urgi.versailles.inra.fr/About-us/News' },
                    { label: 'About us', url: 'https://urgi.versailles.inra.fr/About-us' }
                ]
            },
            {
                label: 'Genomes & Synteny',
                url: '#',
                subMenu: [
                    { label: 'Genomes', url: 'https://urgi.versailles.inra.fr/Data/Genome/Genome-data-access' },
                    { label: 'Synteny', url: 'https://urgi.versailles.inra.fr/synteny/synteny/viewer.do#dataset' }
                ]
            },
            {
                label: 'Genetic resources',
                url: '#',
                subMenu: [
                    { label: 'Plant genetic resources', url: 'https://urgi.versailles.inra.fr/beta/gnpis-core' },
                    { label: 'BRC collections', url: 'https://urgi.versailles.inra.fr/beta/siregal/siregal/grc.do' },
                ]
            },
            {
                label: 'Genetic analyses',
                url: '#',
                subMenu: [
                    { label: 'Genetic maps & QTL', url: 'https://urgi.versailles.inra.fr/beta/GnpMap/mapping/welcome.do' },
                    { label: 'GnpAsso', url: 'https://urgi.versailles.inra.fr/beta/association/association/viewer.do#form' },
                ]
            },
            { label: 'Phenotypes', url: 'https://urgi.versailles.inra.fr/beta/ephesis' },
            { label: 'Polymorphisms', url: 'https://urgi.versailles.inra.fr/beta/GnpSNP/snp/genotyping/form.do' },
            { label: 'Sequences', url: 'https://urgi.versailles.inra.fr/beta/sequence' },
            { label: 'Transcriptomic', url: 'https://urgi.versailles.inra.fr/beta/GnpArray' },

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
