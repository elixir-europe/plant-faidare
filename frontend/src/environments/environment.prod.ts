
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
    }
};
