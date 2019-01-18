interface BrapiData<T> {
    data: T[];
}

export interface BrapiResults<T> {
    metadata: BrapiMetaData;
    result: BrapiData<T>;
}

export interface BrapiMetaData {

    pagination: {
        pageSize: number;
        currentPage: number;
        totalCount: number;
        totalPages: number;
    };

}

interface BrapiStudyData<T> {
    data: T[];
    studyDbId: string;
    studyType: string;
    name: string;
    studyDescription: string;
    seasons: string[];
    startDate: string;
    endDate: string;
    active: boolean;
    trialDbIds: string[];
    location: BrapiLocation;
    contacts: BrapiContacts[];
    additionalInfo: {
        [key: string]: string;
    };
}


export interface BrapiResults<T> {
    metadata: BrapiMetaData;
    result: BrapiStudyData<T>;
}

export interface BrapiMetaData {

    pagination: {
        pageSize: number;
        currentPage: number;
        totalCount: number;
        totalPages: number;
    };

}

export interface BrapiLocation {
    locationDbId: number;
    name: string;
    locationType: string;
    abbreviation: string;
    countryCode: string;
    countryName: string;
    institutionAdress: string;
    institutionName: string;
    altitude: number;
    latitude: number;
    longitude: number;
    additionalInfo?: AdditionalInfo;

}

export interface AdditionalInfo {
    'Site status': string;
    Exposure: string;
    'Distance to city': string;
    'Direction from city': string;
    'Environment type': string;
    Topography: string;
    'Geographical location': string;
    Slope: string;
    'Coordinates precision': string;
    Comment: string;
}


export interface BrapiContacts {
    contactDbId: string;
    name: string;
    email: string;
    type: string;
    institutionName: string;

}

export interface BrapiObservationVariablesData {
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

export interface BrapiObservationUnitsData {
    observationUnitDbId: string;
    observationUnitName: string;
    germplasmDbId: string;
    germplasmName: string;
    studyDbId: string;
    studyName: string;
    programDbId: string;
    programName: string;

}

export interface BrapiGermplasmeData {
    germplasmDbId: string;
    accessionNumber: string;
    germplasmName: string;
    genus: string;
    species: string;
    subtaxa: string;
}

export interface BrapiTrialsResult {
    metadata: {};
    result: {
        trialDbId: string;
        trialName: string;
        trialType: string;
        active: boolean;
        studies: [
            { studyDbId: string; }
            ];
    };
}
