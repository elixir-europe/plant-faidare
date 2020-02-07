export interface XrefModel {

    url: string;
    description: string;
    database_name: string;
    entry_type: string;
    db_version: string;
    name: string;

}

export type XrefResponse = XrefModel[];
