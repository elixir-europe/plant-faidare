import { BrapiDonor, BrapiGermplasm } from './brapi.model';


export interface GermplasmSearchCriteria {
    accessionNumbers: string[];
    germplasmDbIds: string[];
    germplasmGenus: string[];
    germplasmNames: string[];
    germplasmPUIs: string[];
    germplasmSpecies: string[];

    synonyms: string[];
    panel: string[];
    collection: string[];
    population: string[];
    commonCropName: string[];
    species: string[];
    genusSpecies: string[];
    subtaxa: string[];
    genusSpeciesSubtaxa: string[];
    taxonSynonyms: string[];
    biologicalStatus: string[];
    geneticNature: string[];
    holdingInstitute: string[];
    sources: string[];
    types: string;

    facetFields: string[];
    sortBy: string;
    sortOrder: string;
    page: number;
    pageSize: number;
}


export interface Germplasm extends BrapiGermplasm {
    genusSpecies: string;
    genusSpeciesSubtaxa: string;
    taxonSynonyms: string[];
    taxonCommonNames: string[];
    taxonComment: string;
    geneticNature: string;
    comment: string;
    photo: Photo;
    holdingInstitute: Institute;
    holdingGenbank: Institute;
    presenceStatus: string;
    children: Children[];
    originSite: Site;
    collectingSite: Site;
    evaluationSites: Site[];
    collector: GermplasmInstitute;
    breeder: GermplasmInstitute;
    donors: Donor[];
    distributors: GermplasmInstitute[];
    panel: GermplasmSet[];
    collection: GermplasmSet[];
    population: GermplasmSet[];
    'schema:includedInDataCatalog': any;
}

export interface Site {
    latitude: number;
    longitude: number;
    siteId: string;
    siteName: string;
    siteType: string;
}

export interface Institute {
    instituteName: string;
    instituteCode: string;
    acronym: string;
    organisation: string;
    instituteType: string;
    webSite: string;
    address: string;
    logo: string;
}

export interface GermplasmInstitute extends Institute {
    institute: Institute;
    accessionNumber: string;
    accessionCreationDate: string;
    materialType: string;
    collectors: string;
    registrationYear: string;
    deregistrationYear: string;
    distributionStatus?: string;
}

export interface Photo {
    copyright: string;
    description: string;
    file: string;
    photoName: string;
    thumbnailFile: string;

}

export interface Children {
    firstParentName: string;
    firstParentPUI: string;
    secondParentName: string;
    secondParentPUI: string;
    sibblings: {
        name: string;
        pui: string;
    }[];

}

export interface Donor extends BrapiDonor {
    donorInstitute: Institute;
}


export interface GermplasmSet {
    id: number;
    name: string;
    type: string;
    germplasmCount: number;
    germplasmRef: {
        pui: string;
        name: string;
    };
}
