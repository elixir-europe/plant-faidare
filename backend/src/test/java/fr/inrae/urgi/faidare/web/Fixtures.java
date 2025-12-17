package fr.inrae.urgi.faidare.web;

import fr.inrae.urgi.faidare.config.DataSource;
import fr.inrae.urgi.faidare.domain.*;
import fr.inrae.urgi.faidare.domain.brapi.v1.*;
import fr.inrae.urgi.faidare.domain.brapi.v2.*;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableV1VO;
import fr.inrae.urgi.faidare.domain.variable.TraitVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class to create test fixtures
 * @author JB Nizet
 */
public class Fixtures {

    public static LocationVO createSite() {
        LocationVO site = new LocationVO();
        site.setLocationDbId("france");
        site.setLocationName("France");
        site.setSourceUri("https://urgi.versailles.inrae.fr/gnpis");
        site.setUri("Test URI");
        site.setUrl("https://google.com");
        site.setLatitude(45.65);
        site.setLongitude(1.34);
        return site;
    }

    public static LocationV2VO createSiteV2() {
        LocationV2VO site = new LocationV2VO();
        site.setLocationDbId("france");
        site.setLocationName("France");
        site.setSourceUri("https://urgi.versailles.inrae.fr/gnpis");
        site.setUri("Test URI");
        site.setUrl("https://google.com");
        site.setLatitude(45.65);
        site.setLongitude(1.34);

        // FIXME JBN uncomment this once LocationVO has additionalInfo
//        BrapiAdditionalInfo additionalInfo = new BrapiAdditionalInfo();
//        additionalInfo.addProperty("Slope", 4.32);
//        additionalInfo.addProperty("Distance to city", "3 km");
//        additionalInfo.addProperty("foo", "bar");
//        additionalInfo.addProperty("baz", "zing");
//        additionalInfo.addProperty("blob", null);
//        site.setAdditionalInfo(additionalInfo);
        return site;
    }

    public static XRefDocumentVO createXref(String name) {
        XRefDocumentVO xref = new XRefDocumentVO();
        xref.setName(name);
        xref.setDescription("A very large description for the xref " + name + " which has way more than 120 characters bla bla bla bla bla bla bla bla bla bla bla bla");
        xref.setDatabaseName("db_" + name);
        xref.setUrl("https://google.com");
        xref.setEntryType("type " + name);
        return xref;
    }

    public static DataSource createDataSource() {
        return new DataSource(
            "Test URI",
            "Some Data Source",
            "https://google.fr",
            "https://images.com/image.png"
        );
    }

    public static StudyV2VO createStudy() {
        StudyV2VO study = new StudyV2VO();
        study.setStudyDbId("study1");
        // FIXME JBN uncomment this once study has a uri
        // study.setUri("Test URI");
        study.setStudyName("Study 1");
        study.setStudyType("Doability");
        study.setProgramName("Program 1");
        study.setActive(true);
        // FIXME JBN study.startDate used to be a java.util.Date, it's now a String. What does it contain?
        study.setStartDate(String.valueOf(LocalDate.now()));
        study.setDataLinks(List.of(createDataLink()));
        study.setContacts(List.of(createContact()));

        // FIXME JBN uncomment this once study has additionalInfo
//        BrapiAdditionalInfo additionalInfo = new BrapiAdditionalInfo();
//        additionalInfo.addProperty("foo", "bar");
//        additionalInfo.addProperty("baz", "zing");
//        additionalInfo.addProperty("blob", null);
//        study.setAdditionalInfo(additionalInfo);

        study.setLocationDbId("france");
        study.setLocationName("France");
        study.setGermplasmDbIds(List.of("germplasm1"));
        study.setTrialDbIds(Collections.singleton("trial1"));
        study.setUrl("http://test.com/study/");
        return study;
    }

    public static ContactVO createContact() {
        ContactVO contact = new ContactVO();
        contact.setType("Pro");
        contact.setName("John Doe");
        contact.setEmail("john@doe.com");
        contact.setInstituteName("UCLA");
        return contact;
    }

    private static DataLinksVO createDataLink() {
        DataLinksVO dataLink = new DataLinksVO();
        dataLink.setName("Link 1");
        dataLink.setUrl("http://inrae.fr");
        return dataLink;
    }

    public static HtmlContentResultMatchers htmlContent() {
        return new HtmlContentResultMatchers();
    }

    public static GermplasmV2VO createGermplasm() {
        GermplasmV2VO germplasm = new GermplasmV2VO();

        germplasm.setGermplasmDbId("germplasm1");
        germplasm.setGermplasmName("BLE BARBU DU ROUSSILLON");
        germplasm.setAccessionNumber("1408");
        germplasm.setSynonyms(List.of(
            new SynonymsVO("BLE DU ROUSSILLON"),
            new SynonymsVO("FRA051:1699"),
            new SynonymsVO("ROUSSILLON")
        ));

        PhotoVO photo = new PhotoVO();
        photo.setPhotoName("Blé du roussillon");
        photo.setCopyright("INRA, Emmanuelle BOULAT/Lionel BARDY 2012");
        photo.setThumbnailFile("https://urgi.versailles.inrae.fr/files/siregal/images/accession/CEREALS/thumbnails/thumb_1408_R09_S.jpg");
        photo.setFile("https://urgi.versailles.inrae.fr/files/siregal/images/accession/CEREALS/1408_R09_S.jpg");
        germplasm.setPhoto(photo);

        InstituteVO holdingGenBank = new InstituteVO();
        holdingGenBank.setLogo("https://urgi.versailles.inra.fr/files/siregal/images/grc/inra_brc_en.png");
        holdingGenBank.setInstituteName("INRA BRC");
        holdingGenBank.setWebSite("http://google.fr");
        germplasm.setHoldingGenbank(holdingGenBank);

        germplasm.setBiologicalStatusOfAccessionCode("Traditional cultivar/landrace ");
        germplasm.setPedigree("LV");
        SiteVO originSite = new SiteVO("1234",47.0,12.0,"Le Moulon", "Origin Site");
        germplasm.setOriginSite(originSite);

        List<SiteVO> evaluationSites = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SiteVO evaluationSite = new SiteVO();
            evaluationSite.setSiteId(Integer.toString(12347 + i));
            evaluationSite.setSiteType("Evaluation site");
            evaluationSite.setSiteName("Site " + i);
            evaluationSite.setLatitude(46.0 + i);
            evaluationSite.setLongitude(13.0 + i);
            evaluationSites.add(evaluationSite);
        }
        germplasm.setEvaluationSites(evaluationSites);

        germplasm.setGenus("Genus 1");
        germplasm.setSpecies("Species 1");
        germplasm.setSpeciesAuthority("Species Auth");

        germplasm.setSourceUri("https://urgi.versailles.inrae.fr/gnpis");

        germplasm.setSubtaxa("Subtaxa 1");
        germplasm.setGenusSpeciesSubtaxa("Triticum aestivum subsp. aestivum");
        germplasm.setSubtaxaAuthority("INRAE");

        // FIXME JBN uncomment this once germplasm has taxon IDs
        // germplasm.setTaxonIds(List.of(createTaxonId(), createTaxonId()));

        // FIXME JBN uncomment this once germplasm has taxon comment
        // germplasm.setTaxonComment("C'est bon le blé");
        germplasm.setTaxonCommonNames(List.of("Blé tendre", "Bread wheat", "Soft wheat"));

        // FIXME JBN uncomment this once germplasm has taxon synonyms
        // germplasm.setTaxonSynonyms(List.of("Blé tendre1", "Bread wheat1", "Soft wheat1"));

        InstituteVO holdingInstitute = new InstituteVO();
        holdingInstitute.setInstituteName("GDEC - UMR Génétique, Diversité et Ecophysiologie des Céréales");
        holdingInstitute.setLogo("https://urgi.versailles.inra.fr/files/siregal/images/grc/inra_brc_en.png");
        holdingInstitute.setWebSite("https://google.fr/q=qsdqsdqsdslqlsdnqlsdqlsdlqskdlqdqlsdqsdqsdqd");
        holdingInstitute.setInstituteCode("GDEC");
        holdingInstitute.setInstituteType("Type1");
        holdingInstitute.setAcronym("G.D.E.C");
        holdingInstitute.setAddress("Lyon");
        holdingInstitute.setOrganisation("SAS");
        germplasm.setHoldingInstitute(holdingInstitute);

        germplasm.setPresenceStatus("Maintained");

        GermplasmInstituteVO collector = new GermplasmInstituteVO();
        collector.setMaterialType("Fork");
        collector.setCollectors("Joe, Jack, William, Averell");
        InstituteVO collectingInstitute = new InstituteVO();
        collectingInstitute.setInstituteName("Ninja Squad");
        collector.setInstitute(collectingInstitute);
        collector.setAccessionNumber("567");
        germplasm.setCollector(collector);

        SiteVO collectingSite = new SiteVO();
        collectingSite.setSiteId("1235");
        collectingSite.setSiteName("St Just");
        collectingSite.setSiteType("Collecting site");
        collectingSite.setLatitude(48.0);
        collectingSite.setLongitude(13.0);
        germplasm.setCollectingSite(collectingSite);

        germplasm.setAcquisitionDate("In the summer");

        GermplasmInstituteVO breeder = new GermplasmInstituteVO();
        InstituteVO breedingInstitute = new InstituteVO();
        breedingInstitute.setInstituteName("Microsoft");
        breeder.setInstitute(breedingInstitute);
        breeder.setAccessionCreationDate(2015);
        breeder.setAccessionNumber("678");
        breeder.setRegistrationYear(2016);
        breeder.setDeregistrationYear(2019);
        germplasm.setBreeder(breeder);

        germplasm.setDonors(List.of(createDonor()));

        germplasm.setDistributors(List.of(createDistributor()));

        germplasm.setChildren(List.of(createChild(), createChild()));

        germplasm.setGermplasmPUI("germplasmPUI");
        germplasm.setPopulation(List.of(createPopulation1(), createPopulation2(), createPopulation3()));

        germplasm.setCollection(List.of(createCollection()));

        germplasm.setPanel(List.of(createPanel()));

        return germplasm;
    }

    public static GermplasmV2VO createGermplasmMinimal() {
        GermplasmV2VO germplasm = new GermplasmV2VO();

        germplasm.setGermplasmDbId("germplasm-mini1");
        germplasm.setGermplasmName("BLE BARBU DU ROUSSILLON mini");
        germplasm.setAccessionNumber("1408-mini");

        InstituteVO holdingGenBank = new InstituteVO();
        holdingGenBank.setLogo("https://urgi.versailles.inra.fr/files/siregal/images/grc/inra_brc_en.png");
        holdingGenBank.setInstituteName("INRA BRC");
        holdingGenBank.setWebSite("http://google.fr");
        germplasm.setHoldingGenbank(holdingGenBank);

        germplasm.setBiologicalStatusOfAccessionCode("Traditional cultivar/landrace ");

        germplasm.setGenus("Genus 1");
        germplasm.setSpecies("Species 1");
        // FIXME JBN uncomment this once germplasm has taxon IDs
        // germplasm.setTaxonIds(List.of(createTaxonId(), createTaxonId()));

        InstituteVO holdingInstitute = new InstituteVO();
        holdingInstitute.setInstituteName("GDEC - UMR Génétique, Diversité et Ecophysiologie des Céréales");
        holdingInstitute.setInstituteCode("GDEC");
        holdingInstitute.setInstituteType("Type1");
        holdingInstitute.setAcronym("G.D.E.C");
        holdingInstitute.setOrganisation("SAS");
        germplasm.setHoldingInstitute(holdingInstitute);

        germplasm.setPresenceStatus("Maintained");

        germplasm.setGermplasmPUI("germplasmPUI mini");

        return germplasm;
    }

    public static GermplasmV2VO createGermplasmV2ForTrial() {
        GermplasmV2VO germplasm = new GermplasmV2VO();

        germplasm.setGermplasmDbId("germplasm-mini1");
        germplasm.setGermplasmName("BLE BARBU DU ROUSSILLON mini");
        germplasm.setAccessionNumber("1408-mini");

        germplasm.setGenus("Genus 1");
        germplasm.setSpecies("Species 1");
        germplasm.setSubtaxa("Subtaxa 1");
        return germplasm;
    }

    private static DonorVO createDonor() {
        DonorVO result = new DonorVO();
        result.setDonorGermplasmPUI("PUI1");
        result.setDonationDate(2017);
        result.setDonorAccessionNumber("3456");
        result.setDonorInstituteCode("GD46U");
        InstituteVO institute = new InstituteVO();
        institute.setInstituteName("Hello");
        result.setDonorInstitute(institute);
        return result;
    }

    private static GermplasmInstituteVO createDistributor() {
        GermplasmInstituteVO result = new GermplasmInstituteVO();
        InstituteVO institute = new InstituteVO();
        institute.setInstituteName("Microsoft");
        result.setInstitute(institute);
        result.setAccessionNumber("678");
        result.setDistributionStatus("OK");
        return result;
    }

    private static GermplasmPedigreeV1VO createPedigree() {
        GermplasmPedigreeV1VO result = new GermplasmPedigreeV1VO();
        result.setPedigree("Pedigree 1");
        result.setParent1DbId("12345");
        result.setParent1Name("Parent 1");
        result.setParent1Type("P1");
        result.setParent2DbId("12346");
        result.setParent2Name("Parent 2");
        result.setParent2Type("P2");
        result.setCrossingPlan("crossing plan 1");
        result.setCrossingYear("2012");
        result.setSiblings(List.of(createBrapiSibling()));
        return result;
    }

    private static SiblingV1VO createBrapiSibling() {
        SiblingV1VO sibling = new SiblingV1VO();
        sibling.setGermplasmDbId("5678");
        sibling.setDefaultDisplayName("Sibling 5678");
        return sibling;
    }

    private static GenealogyVO createChild() {
        GenealogyVO result = new GenealogyVO();
        result.setFirstParentName("CP1");
        result.setSecondParentName("CP2");
        result.setSibblings(List.of(createPuiNameValueVO(), createPuiNameValueVO()));
        return result;
    }

    private static PuiNameValueVO createPuiNameValueVO() {
        PuiNameValueVO result = new PuiNameValueVO();
        result.setName("Child 1");
        result.setPui("pui1");
        return result;
    }

    private static CollPopVO createPopulation1() {
        CollPopVO result = new CollPopVO("Population 1", "collpop1");
        result.setName("Population 1");
        result.setType("Pop Type 1");
        result.setGermplasmCount(3);
        result.setGermplasmRef(createPuiNameValueVO());
        return result;
    }

    private static CollPopVO createPopulation2() {
        CollPopVO result = new CollPopVO("Population 2", "collpop2");
        result.setName("Population 2");
        result.setGermplasmCount(3);
        PuiNameValueVO puiNameValueVO = createPuiNameValueVO();
        puiNameValueVO.setPui("germplasmPUI");
        result.setGermplasmRef(puiNameValueVO);
        return result;
    }

    private static CollPopVO createPopulation3() {
        CollPopVO result = new CollPopVO("Population 3", "collpop3");
        result.setName("Population 3");
        result.setGermplasmCount(5);
        return result;
    }

    private static CollPopVO createCollection() {
        CollPopVO result = new CollPopVO("Collection 1", "collpop4");
        result.setName("Collection 1");
        result.setGermplasmCount(7);
        return result;
    }

    private static CollPopVO createPanel() {
        CollPopVO result = new CollPopVO("The_panel_1", "collpop5");
        result.setName("The_panel_1");
        result.setGermplasmCount(2);
        return result;
    }

    private static TaxonSourceVO createTaxonId() {
        TaxonSourceVO result = new TaxonSourceVO();
        result.setTaxonId("taxon1");
        result.setSourceName("ThePlantList");
        return result;
    }

    public static TrialV2VO createTrial() {
        TrialV2VO trial = new TrialV2VO();
        trial.setTrialName("Trail 1");
        trial.setTrialType("Trial type 1");
        trial.setDocumentationURL("http://trials.com");
        trial.setStudies(List.of(createTrialV2Study()));
        return trial;
    }

    public static TrialV2VO createTrialV2() {
        TrialV2VO trial = new TrialV2VO();
        trial.setTrialDbId("trial1");
        trial.setTrialName("Trial 1");
        trial.setTrialType("Trial type 1");
        trial.setDocumentationURL("http://trials.com");
        trial.setContact(List.of(createContact()));
        trial.setStudies(List.of(createTrialV2Study()));

        trial.setProgramName("Program 1");
        // FIXME JBN trial.startDate should be a LocalDate, not a String
        // answer: on the Brapi starDate and endDate are Strings
        trial.setStartDate(LocalDate.of(2020, 1, 1).toString());
        trial.setEndDate(LocalDate.of(2022, 1, 1).toString());

        return trial;
    }

    private static StudyV1miniVO createTrialStudy() {
        StudyV1miniVO study = new StudyV1miniVO();
        study.setStudyDbId("study2");
        study.setStudyName("Study 2");
        LocationVO site = createSite();
        study.setLocationDbId(site.getLocationDbId());
        study.setLocationName(site.getLocationName());

        return study;
    }

    private static StudyV2miniVO createTrialV2Study() {
        StudyV2miniVO study = new StudyV2miniVO();
        study.setStudyDbId("study2");
        study.setStudyName("Study 2");

        LocationVO site = createSite();
        study.setLocationDbId(site.getLocationDbId());
        study.setLocationName(site.getLocationName());

        return study;
    }

    public static GermplasmAttributeV1VO createGermplasmAttribute() {
        GermplasmAttributeValueV1VO value = new GermplasmAttributeValueV1VO();
        value.setAttributeName("A1");
        value.setValue("V1");

        GermplasmAttributeV1VO germplasmAttribute = new GermplasmAttributeV1VO();
        germplasmAttribute.setData(List.of(value));
        return germplasmAttribute;
    }

    public static ObservationVariableV1VO createVariable() {
        ObservationVariableV1VO variable = new ObservationVariableV1VO();
        variable.setObservationVariableDbId("variable1");
        variable.setDocumentationURL("http://variables.com");
        variable.setName("Variable 1");
        variable.setSynonyms(List.of("V1"));
        variable.setOntologyName("Ontology 1");
        TraitVO trait = new TraitVO();
        trait.setDescription("Trait 1");
        variable.setTrait(trait);
        return variable;
    }

    public static GermplasmMcpdVO createGermplasmMcpd() {
        GermplasmMcpdVO result = new GermplasmMcpdVO();
        result.setPUID("PUI1");
        result.setInstituteCode("Inst1");
        return result;
    }
}
