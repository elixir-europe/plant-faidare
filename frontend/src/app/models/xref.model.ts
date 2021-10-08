export interface XrefModel {

    url: string;
    description: string;
    databaseName: string;
    entryType: string;
    identifier: string;
    name: string;

}

export type XrefResponse = XrefModel[];
