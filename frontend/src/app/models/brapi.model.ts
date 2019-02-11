export interface BrapiMetaData {
    pagination: {
        pageSize: number;
        currentPage: number;
        totalCount: number;
        totalPages: number;
    };
}


export interface BrapiResult<T> {
    metadata: BrapiMetaData;
    result: T;
}


interface BrapiData<T> {
    data: T[];
}


export interface BrapiResults<T> extends BrapiResult<BrapiData<T>> {
}


interface BrapiHasDocumentationURL {
    documentationURL?: string;
}


export interface BrapiStudy extends BrapiHasDocumentationURL {
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
    contacts: BrapiContacts[];
    additionalInfo: AdditionalInfo;
    dataLinks: {
        name: string;
        type: string;
        url: string;
    }[];
}


export interface BrapiLocation extends BrapiHasDocumentationURL  {
    locationDbId: string;
    locationName: string;
    locationType: string;
    abbreviation: string;
    countryCode: string;
    countryName: string;
    institutionAddress: string;
    institutionName: string;
    altitude: number;
    latitude: number;
    longitude: number;
    additionalInfo?: AdditionalInfo;
}


export interface AdditionalInfo {
    [key: string]: string;
}


export interface BrapiContacts {
    contactDbId: string;
    name: string;
    email: string;
    type: string;
    institutionName: string;
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


export interface BrapiGermplasm extends BrapiHasDocumentationURL {
    germplasmDbId: string;
    accessionNumber: string;
    germplasmName: string;
    genus: string;
    species: string;
    subtaxa: string;
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
