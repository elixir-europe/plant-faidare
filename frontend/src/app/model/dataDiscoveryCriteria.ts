
export interface DataDiscoveryCriteria {
    accessions: string[];
    crops: string[];
    germplasmLists: string[];
    observationVariableIds: string[];

    /**
     * Used to store the top selected node ids from the trait ontology widget
     */
    topSelectedTraitOntologyIds: string[];

    page: number;
    pageSize: number;
}

export function emptyCriteria(): DataDiscoveryCriteria {
    return {
        accessions: null,
        crops: null,
        germplasmLists: null,
        observationVariableIds: null,
        topSelectedTraitOntologyIds: null,

        page: 0,
        pageSize: 10
    };
}
