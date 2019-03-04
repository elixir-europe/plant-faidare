import { BrapiDescriptor, BrapiDonor, BrapiSet } from './brapi.germplasm.model';

export interface Site {
    latitude: number;
    longitude: number;
    siteId: number;
    siteName: string;
    siteType: string;
}

export interface Germplasm {
    source: string;
    url: string;
    germplasmDbId: string;
    defaultDisplayName: string;
    accessionNumber: string;
    germplasmName: string;
    germplasmPUI: string;
    pedigree: string;
    seedSource: string;
    synonyms: string;
    commonCropName: string;
    instituteCode: string;
    instituteName: string;
    biologicalStatusOfAccessionCode: string;
    countryOfOriginCode: string;
    typeOfGermplasmStorageCode: string;
    taxonIds: string;
    genus: string;
    species: string;
    speciesAuthority: string;
    subtaxa: string;
    subtaxaAuthority: string;
    donors: BrapiDonor[];
    acquisitionDate: string;
    genusSpecies: string;
    genusSpeciesSubtaxa: string;
    taxonSynonyms: string[];
    taxonCommonNames: string[];
    geneticNature: string;
    comment: string;
    photo: string;
    holdingInstitute: Institute;
    holdingGenbank: Institute;
    presenceStatus: string;
    children: string;
    descriptors: BrapiDescriptor[];
    originSite: Site;
    collectingSite: Site;
    evaluationSites: Site[];
    collector: Origin;
    breeder: Origin;
    distributors: Origin[];
    panel: BrapiSet[];
    collection: BrapiSet[];
    population: BrapiSet[];
}

export interface Origin {
    institute: Institute;
    germplasmPUI: string;
    accessionNumber: string;
    accessionCreationDate: string;
    materialType: string;
    collectors: string;
    registrationYear: string;
    deregistrationYear: string;
    distributionStatus: string;
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

export interface GermplasmData<T> {
    data: T;
}

export interface GermplasmResult<T> {
    result: T;
}



