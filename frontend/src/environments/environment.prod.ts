
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
};
