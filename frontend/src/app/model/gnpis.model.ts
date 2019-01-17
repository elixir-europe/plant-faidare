import { BrapiDescriptor, BrapiDonor, BrapiInstitute, BrapiOrigin, BrapiSet, BrapiSite } from './brapi.model';

export interface GermplasmData<T> {
    data: T;
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
    holdingInstitute: BrapiInstitute;
    holdingGenbank: BrapiInstitute;
    presenceStatus: string;
    children: string;
    descriptors: BrapiDescriptor[];
    originSite: BrapiSite;
    collectingSite: BrapiSite;
    evaluationSites: BrapiSite[];
    collector: BrapiOrigin;
    breeder: BrapiOrigin;
    distributors: BrapiOrigin[];
    panel: BrapiSet[];
    collection: BrapiSet[];
    population: BrapiSet[];
}

export interface GermplasmResult<T> {
    result: GermplasmData<T>;
}

export interface GermplasmRef {
    name: string;
    pui: string;
    value: string;
}



