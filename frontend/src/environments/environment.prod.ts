
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
