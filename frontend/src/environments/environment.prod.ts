
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
                label: 'Taxon/Germplasm',
                url: '#',
                subMenu: [
                    { label: 'Taxon', url: 'https://urgi.versailles.inra.fr/siregal/common/taxon/form.do' },
                    { label: 'Accession Simple', url: 'https://urgi.versailles.inra.fr/gnpis-core' },
                    { label: 'Accession passport', url: 'https://urgi.versailles.inra.fr/siregal/siregal/accessionForm.do' },
                    { label: 'Collections CRB', url: 'https://urgi.versailles.inra.fr/siregal/siregal/grc.do' },
                ]
            },
            { label: 'Phenotyping', url: 'https://urgi.versailles.inra.fr/ephesis/ephesis/viewer.do' },
            {
                label: 'Polymorphism',
                url: '#',
                subMenu: [
                    { label: 'Genotyping', url: 'https://urgi.versailles.inra.fr/GnpSNP/snp/genotyping/form.do' },
                    { label: 'SNP Discovery', url: 'https://urgi.versailles.inra.fr/GnpSNP/snp/welcome.do' },
                ]
            },
            { label: 'Association', url: 'https://urgi.versailles.inra.fr/association/association/viewer.do#form' },
            {
                label: 'Map/Marker/QTL',
                url: '#',
                subMenu: [
                    { label: 'Map', url: 'https://urgi.versailles.inra.fr/GnpMap/mapping/searchMap.do' },
                    { label: 'Loci', url: 'https://urgi.versailles.inra.fr/GnpMap/mapping/loci/queryLociSelect.do' },
                    { label: 'QTL', url: 'https://urgi.versailles.inra.fr/GnpMap/mapping/qtl/queryQtlSelect.do' },
                    { label: 'MetaQTLs', url: 'https://urgi.versailles.inra.fr/GnpMap/mapping/metaqtl/form.do' },
                    { label: 'Marker', url: 'https://urgi.versailles.inra.fr/GnpMap/mapping/marker/markerForm.do' },
                    { label: 'Pool', url: 'https://urgi.versailles.inra.fr/GnpMap/mapping/pool/poolForm.do' },
                    { label: 'Traits', url: 'https://urgi.versailles.inra.fr/GnpMap/mapping/queryTraitSelect.do' },
                    { label: 'Biomercator', url: 'https://urgi.versailles.inra.fr/Tools/BioMercator-V4' },
                ]
            },
            { label: 'Genomes', url: 'https://urgi.versailles.inra.fr/Data/Genome/Genome-data-access' },
            { label: 'Synteny', url: 'https://urgi.versailles.inra.fr/synteny/synteny/viewer.do#dataset' },
            {
                label: 'Sequence',
                url: '#',
                subMenu: [
                    { label: 'Sequence', url: 'https://urgi.versailles.inra.fr/sequence/sequence/sequence/form.do' },
                    { label: 'Experiment', url: 'https://urgi.versailles.inra.fr/sequence/sequence/experiment/form.do' },
                    { label: 'Analysis', url: 'https://urgi.versailles.inra.fr/sequence/sequence/analysis/form.do' },
                    { label: 'Project', url: 'https://urgi.versailles.inra.fr/sequence/sequence/project/form.do' },
                ]
            }

        ]
    }
};
