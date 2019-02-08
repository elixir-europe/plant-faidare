import { Institute } from './gnpis.germplasm.model';

export interface BrapiSite {
    latitude: number;
    longitude: number;
    siteId: number;
    siteName: string;
    siteType: string;
}

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
    donorInstitute: Institute;
    germplasmPUI: string;
    accessionNumber: string;
    donorInstituteCode: string;
}

export interface BrapiSet {
    germplasmCount: number;
    germplasmRef: string;
    id: number;
    name: string;
    type: string;
}
