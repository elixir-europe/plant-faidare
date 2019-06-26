
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
        ThePlantList: 'http://www.theplantlist.org/tpl1.1/record/',
        TAXREF: 'https://inpn.mnhn.fr/espece/cd_nom/',
        CatalogueOfLife: 'http://www.catalogueoflife.org/col/details/species/id/'
    }
};
