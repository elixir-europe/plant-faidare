// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
    production: false,
    navbar: {
        name: 'FAIDARE',
        title: 'FAIR Data-finder for Agronomic REsearch',
        logo: 'assets/applicationLogo.png',
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
            {
                label: 'Data providers',
                url: '#',
                subMenu: [
                    { label: 'GNPIS', url: 'https://urgi.versailles.inra.fr/gnpis/' },
                    { label: 'CIRAD TropGENE', url: 'http://tropgenedb.cirad.fr/' },
                    { label: 'VIB Pippa', url: 'http://pippa.psb.ugent.be' },
                    { label: 'IBET BioData', url: 'https://biodata.pt' }
                ]
            }
        ],
        contributor: {
            name: 'Elixir',
            url: 'https://elixir-europe.org/',
            logo: 'assets/elixir_logo.png'
        }
    },
    helpMdFile: 'assets/help.md',
    aboutUsMdFile: 'assets/about.md',
    joinUsMdFile: 'assets/join.md',
    legalMentionsMdFile: 'assets/legal.md',
    taxaLinks: {
        NCBI: 'https://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=',
        ThePlantList:'http://www.theplantlist.org/tpl1.1/record/',
        TAXREF: 'https://inpn.mnhn.fr/espece/cd_nom/',
        CatalogueOfLife:'http://www.catalogueoflife.org/col/details/species/id/'
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
