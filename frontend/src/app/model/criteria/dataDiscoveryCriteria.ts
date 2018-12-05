export interface DataDiscoveryCriteria {
    accessions: string[];
    crops: string[];
    germplasmLists: string[];
}

export const EMPTY_CRITERIA: DataDiscoveryCriteria = {
    accessions: [],
    crops: [],
    germplasmLists: []
};
