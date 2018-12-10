export interface DataDiscoveryCriteria {
    accessions: string[];
    crops: string[];
    germplasmLists: string[];

    page: number;
    pageSize: number;
}

export function newCriteria(): DataDiscoveryCriteria {
    return {
        accessions: [],
        crops: [],
        germplasmLists: [],

        page: 0,
        pageSize: 10
    };
}
