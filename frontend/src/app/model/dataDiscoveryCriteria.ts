export interface DataDiscoveryCriteria {
    accessions: string[];
    crops: string[];
    germplasmLists: string[];
}

export function newCriteria(): DataDiscoveryCriteria {
    return {
        accessions: [],
        crops: [],
        germplasmLists: []
    };
}
