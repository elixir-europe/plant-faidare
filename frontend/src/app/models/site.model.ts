export interface SiteModel {
    result: SiteResultModel;
}

export interface SiteResultModel {
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
