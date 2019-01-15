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
    location: BrapiLocation;
    contacts: BrapiContacts[];
}


export interface BrapiLocation {
    locationDbId: string;
    name: string;
    locationType: string;
    countryCode: string;
    countryName: string;
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
}
