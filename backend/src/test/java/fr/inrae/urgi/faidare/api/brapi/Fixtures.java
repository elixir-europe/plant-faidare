package fr.inrae.urgi.faidare.api.brapi;

import fr.inrae.urgi.faidare.domain.CollPopVO;
import fr.inrae.urgi.faidare.domain.InstituteVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;
import fr.inrae.urgi.faidare.domain.SiteVO;

import java.util.*;


public class Fixtures {
    public static GermplasmV1VO createGermplasm() {
        // Créer un nouvel objet Germplasm
        GermplasmV1VO germplasm = new GermplasmV1VO();

        // Remplir les propriétés basées sur le JSON
        germplasm.setGenus("Triticum");
        germplasm.setSpecies("aestivum");
        germplasm.setSubtaxa("subsp. aestivum");
        germplasm.setGenusSpecies("Triticum aestivum");
        germplasm.setGenusSpeciesSubtaxa("Triticum aestivum subsp. aestivum");
        germplasm.setCommonCropName("Wheat");
        germplasm.setGroupId(0L);
        germplasm.setAccessionNames(List.of("AO14022"));
        germplasm.setAccessionNumber("AO14022");
        germplasm.setDefaultDisplayName("AO14022");
        germplasm.setId("urn:INRAE-URGI/germplasm/61039");
        germplasm.setType("germplasm");
        germplasm.setSchemaId("urn:INRAE-URGI/germplasm/61039");
        germplasm.setSchemaName("AO14022");
        germplasm.setSchemaCatalog("https://urgi.versailles.inrae.fr/gnpis");


        // Panel
        germplasm.setPanel(createPanel());

        // Collection
        germplasm.setCollection(List.of(createCollection()));

        // studyDbIds
        List<String> studyDbIds = List.of(
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0NsZXJtb250LUZlcnJhbmRfMjAxNF9TZXRCMg==",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0NsZXJtb250LUZlcnJhbmRfMjAxNF9URUNI",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0NsZXJtb250LUZlcnJhbmRfMjAxNV9TZXRBMg==",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0Rpam9uXzIwMTRfU2V0QjI=",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0Rpam9uXzIwMTRfVEVDSA==",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0Rpam9uXzIwMTVfU2V0QTI=",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMTRfU2V0QjI=",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMTRfVEVDSA==",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0VzdHIlQzMlQTllcy1Nb25zXzIwMTVfU2V0QTI=",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0xlX01vdWxvbl8yMDE0X1NldEIy",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0xlX01vdWxvbl8yMDE0X1RFQ0g=",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0xlX01vdWxvbl8yMDE1X1NldEEy",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0x1c2lnbmFuXzIwMTRfU2V0QjI=",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0x1c2lnbmFuXzIwMTRfVEVDSA==",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX0x1c2lnbmFuXzIwMTVfU2V0QTI=",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX09yZ2V2YWxfMjAxNF9TZXRCMg==",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX09yZ2V2YWxfMjAxNV9TZXRBMg==",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX1Jlbm5lc18yMDE0X1NldEIy",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX1Jlbm5lc18yMDE0X1NldEIyX1BJRVRJTi1WRVJTRQ==",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX1Jlbm5lc18yMDE0X1RFQ0g=",
            "dXJuOklOUkFFLVVSR0kvc3R1ZHkvQlRIX1Jlbm5lc18yMDE1X1NldEEy"
        );
        germplasm.setStudyDbIds(studyDbIds);

        // Remplir la section "EvaluationSites"
        List<SiteVO> evaluationSites = new ArrayList<>();
        evaluationSites.add(new SiteVO("1994", 45.773, 3.144, "Clermont-Ferrand", "Breeding and Evaluation site"));
        evaluationSites.add(new SiteVO("32824", 46.4, 0.07, "Lusignan", "Breeding and Evaluation site"));
        evaluationSites.add(new SiteVO("33428", 49.87857, 3.007548, "Estrées-Mons", "Evaluation site"));
        evaluationSites.add(new SiteVO("33818", 47.277, 5.094, "Dijon", "Collecting and Evaluation site"));
        evaluationSites.add(new SiteVO("33985", 48.106, -1.791, "Rennes", "Collecting and Evaluation site"));
        evaluationSites.add(new SiteVO("34064", 48.711, 2.16, "Le Moulon", "Evaluation site"));
        evaluationSites.add(new SiteVO("34065", 48.838, 1.953, "Orgeval", "Evaluation site"));
        germplasm.setEvaluationSites(evaluationSites);

        // Infos sur l'institut
        InstituteVO holdingInstitute = new InstituteVO();
        holdingInstitute.setInstituteCode("FRA040");
        holdingInstitute.setInstituteName("GDEC - UMR Génétique, Diversité et Ecophysiologie des Céréales");
        holdingInstitute.setInstituteType("Public-sector research organization");
        holdingInstitute.setWebSite("https://www6.clermont.inrae.fr/umr1095");
        holdingInstitute.setAddress("5 Chemin de Beaulieu, 63039 CLERMONT-FERRAND Cedex 2, France");
        germplasm.setHoldingInstitute(holdingInstitute);

        // Taxon common names
        germplasm.setTaxonCommonNames(List.of("Blé tendre", "Bread wheat", "Soft wheat"));

        // Germplasm URI et autres données
        germplasm.setGermplasmURI("urn:INRAE-URGI/germplasm/61039");
        germplasm.setGermplasmDbId("dXJuOklOUkFFLVVSR0kvZ2VybXBsYXNtLzYxMDM5");
        germplasm.setGermplasmName("AO14022");
        germplasm.setGermplasmPUI("https://doi.org/10.15454/T1DLTW");

        // Remplir le reste des informations nécessaires (comme les études, l'institut de sécurité, etc.)
        germplasm.setStudyURIs(List.of(
            "urn:INRAE-URGI/study/BTH_Clermont-Ferrand_2014_SetB2",
            "urn:INRAE-URGI/study/BTH_Clermont-Ferrand_2014_TECH",
            "urn:INRAE-URGI/study/BTH_Clermont-Ferrand_2015_SetA2",
            "urn:INRAE-URGI/study/BTH_Dijon_2014_SetB2",
            "urn:INRAE-URGI/study/BTH_Dijon_2014_TECH",
            "urn:INRAE-URGI/study/BTH_Dijon_2015_SetA2",
            "urn:INRAE-URGI/study/BTH_Estr%C3%A9es-Mons_2014_SetB2",
            "urn:INRAE-URGI/study/BTH_Estr%C3%A9es-Mons_2014_TECH",
            "urn:INRAE-URGI/study/BTH_Estr%C3%A9es-Mons_2015_SetA2",
            "urn:INRAE-URGI/study/BTH_Le_Moulon_2014_SetB2",
            "urn:INRAE-URGI/study/BTH_Le_Moulon_2014_TECH",
            "urn:INRAE-URGI/study/BTH_Le_Moulon_2015_SetA2",
            "urn:INRAE-URGI/study/BTH_Lusignan_2014_SetB2",
            "urn:INRAE-URGI/study/BTH_Lusignan_2014_TECH",
            "urn:INRAE-URGI/study/BTH_Lusignan_2015_SetA2",
            "urn:INRAE-URGI/study/BTH_Orgeval_2014_SetB2",
            "urn:INRAE-URGI/study/BTH_Orgeval_2015_SetA2",
            "urn:INRAE-URGI/study/BTH_Rennes_2014_SetB2",
            "urn:INRAE-URGI/study/BTH_Rennes_2014_SetB2_PIETIN-VERSE",
            "urn:INRAE-URGI/study/BTH_Rennes_2014_TECH",
            "urn:INRAE-URGI/study/BTH_Rennes_2015_SetA2"
        ));

        return germplasm;
    }



    private static List<CollPopVO> createPanel() {
        CollPopVO result = new CollPopVO("SMALL_GRAIN_CEREALS_NETWORK_COL", "8");
        result.setGermplasmCount(1728);
        CollPopVO result2 = new CollPopVO("BREEDWHEAT_PANEL", "11");
        result.setGermplasmCount(3047);
        return List.of(result,result2);
    }
    private static CollPopVO createCollection() {
        CollPopVO result = new CollPopVO("SMALL_GRAIN_CEREALS_NETWORK_COL", "128");
        result.setGermplasmCount(1410);
        return result;
    }



}
