import { Institute } from './gnpis.germplasm.model';


export interface BrapiSibling {
    germplasmDbId: string;
    defaultDisplayName: string;
}

export interface BrapiDescriptor {
    name: string;
    pui: string;
    value: string;
}

export interface BrapiGermplasmPedigree {
    germplasmDbId: string;
    defaultDisplayName: string;
    pedigree: string;
    crossingPlan: string;
    crossingYear: string;
    familyCode: string;
    parent1DbId: string;
    parent1Name: string;
    parent1Type: string;
    parent2DbId: string;
    parent2Name: string;
    parent2Type: string;
    siblings: BrapiSibling[];
}

export interface BrapiGermplasmProgeny {
    germplasmDbId: string;
    defaultDisplayName: string;
    progeny: BrapiSibling[];
}

export interface BrapiGermplasmAttributes {
    attributeName: string;
    value: string;
}

export interface BrapiDonor {
    donorGermplasmPUI: string;
    donorAccessionNumber: string;
    donorInstituteCode: string;
    donationDate: number;
    donorInstitute: Institute;
}

