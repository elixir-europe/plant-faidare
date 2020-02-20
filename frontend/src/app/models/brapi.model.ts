import * as schema from './schema.org.model';

export interface BrapiMetaData {
    pagination: {
        pageSize: number;
        currentPage: number;
        totalCount: number;
        totalPages: number;
    };
}

/**
 * BrAPI single response
 */
export interface BrapiResult<T> {
    metadata: BrapiMetaData;
    result: T;
}

/**
 * BrAPI list response
 */
export type BrapiResults<T> = BrapiResult<{
    data: T[];
}>;


interface BrapiHasDocumentationURL {
    documentationURL?: string;
}

export interface BrapiStudy extends BrapiHasDocumentationURL, schema.Dataset {
    studyDbId: string;
    studyType: string;
    studyName: string;
    studyDescription: string;
    seasons: string[];
    startDate: string;
    endDate: string;
    active: boolean;
    programDbId: string;
    programName: string;
    trialDbIds: string[];
    location: BrapiLocation;
    locationDbId: string;
    locationName: string;
    contacts: BrapiContacts[];
    additionalInfo: BrapiAdditionalInfo;
    dataLinks: {
        name: string;
        type: string;
        url: string;
    }[];
}


export interface BrapiContacts {
    contactDbId: string;
    name: string;
    email: string;
    type: string;
    institutionName: string;
}


export interface BrapiLocation extends BrapiHasDocumentationURL, schema.Dataset {
    locationDbId: string;
    locationName: string;
    locationType: string;
    abbreviation?: string;
    countryCode?: string;
    countryName?: string;
    instituteAddress?: string;
    instituteName?: string;
    altitude?: number;
    latitude: number;
    longitude: number;
    additionalInfo?: BrapiAdditionalInfo;
}


export interface BrapiAdditionalInfo {
    [key: string]: string;
}


export interface BrapiObservationVariable extends BrapiHasDocumentationURL  {
    observationVariableDbId: string;
    contextOfUse: string[];
    institution: string;
    crop: string;
    name: string;
    ontologyDbId: string;
    ontologyName: string;
    synonyms: string[];
    language: string;
    trait: {
        traitDbId: string;
        name: string;
        description: string;
    };
}


export interface BrapiTrial extends BrapiHasDocumentationURL {
    trialDbId: string;
    trialName: string;
    trialType: string;
    active: boolean;
    studies: {
        studyDbId: string;
        studyName: string;
    }[];
}

export interface BrapiGermplasm extends BrapiHasDocumentationURL, schema.Dataset {
    germplasmDbId: string;
    defaultDisplayName: string;
    accessionNumber: string;
    germplasmName: string;
    germplasmPUI: string;
    pedigree: string;
    seedSource: string;
    synonyms: string[];
    commonCropName: string;
    instituteCode: string;
    instituteName: string;
    biologicalStatusOfAccessionCode: string;
    countryOfOriginCode: string;
    typeOfGermplasmStorageCode: string[];
    taxonIds: BrapiTaxonIds[];
    genus: string;
    species: string;
    speciesAuthority: string;
    subtaxa: string;
    subtaxaAuthority: string;
    donors: BrapiDonor[];
    acquisitionDate: string;
}


export interface BrapiTaxonIds {
    sourceName: string;
    taxonId: string;
    url: string;
}

export interface BrapiDonor {
    donorGermplasmPUI: string;
    donorAccessionNumber: string;
    donorInstituteCode: string;
    donationDate: number;
}

export interface BrapiGermplasmPedigree {
    germplasmDbId: string;
    defaultDisplayName: string;
    pedigree: string;
    crossingPlan: string;
    crossingYear: string;
    familyCode: string;
    parent1DbId: string;
    parent1Name: string;
    parent1Type: string;
    parent2DbId: string;
    parent2Name: string;
    parent2Type: string;
    siblings: BrapiSibling[];
}

export interface BrapiSibling {
    germplasmDbId: string;
    defaultDisplayName: string;
}

export interface BrapiGermplasmProgeny {
    germplasmDbId: string;
    defaultDisplayName: string;
    progeny: BrapiProgeny[];
}

export interface BrapiProgeny {
    germplasmDbId: string;
    defaultDisplayName: string;
    parentType: string;

}


export interface BrapiGermplasmAttributes {
    germplasmDbId: string;
    data: BrapiAttributeData[];
}

export interface BrapiAttributeData  {
    attributeDbId: string;
    attributeName: string;
    attributeCode: string;
    value: string;
    determinedDate: string;
}
