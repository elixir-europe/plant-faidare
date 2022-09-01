package fr.inra.urgi.faidare.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiAdditionalInfo;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiSibling;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiStudyDataLink;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiTrialStudy;
import fr.inra.urgi.faidare.domain.data.ContactVO;
import fr.inra.urgi.faidare.domain.data.LocationVO;
import fr.inra.urgi.faidare.domain.data.TrialStudySummaryVO;
import fr.inra.urgi.faidare.domain.data.TrialVO;
import fr.inra.urgi.faidare.domain.data.germplasm.CollPopVO;
import fr.inra.urgi.faidare.domain.data.germplasm.DonorVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GenealogyVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmAttributeValueListVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmAttributeValueVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmInstituteVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmMcpdVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.InstituteVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PhotoVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PuiNameValueVO;
import fr.inra.urgi.faidare.domain.data.germplasm.SiblingVO;
import fr.inra.urgi.faidare.domain.data.germplasm.SiteVO;
import fr.inra.urgi.faidare.domain.data.germplasm.TaxonSourceVO;
import fr.inra.urgi.faidare.domain.data.study.StudyDataLinkVO;
import fr.inra.urgi.faidare.domain.data.study.StudyDetailVO;
import fr.inra.urgi.faidare.domain.data.variable.ObservationVariableVO;
import fr.inra.urgi.faidare.domain.data.variable.TraitVO;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSource;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSourceImpl;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;

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
        BrapiAdditionalInfo additionalInfo = new BrapiAdditionalInfo();
        additionalInfo.addProperty("Slope", 4.32);
        additionalInfo.addProperty("Distance to city", "3 km");
        additionalInfo.addProperty("foo", "bar");
        additionalInfo.addProperty("baz", "zing");
        additionalInfo.addProperty("blob", null);
        site.setAdditionalInfo(additionalInfo);
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
        DataSourceImpl dataSource = new DataSourceImpl();
        dataSource.setUri("Test URI");
        dataSource.setUrl("https://google.fr");
        dataSource.setName("Some Data Source");
        dataSource.setIdentifier("DS1");
        dataSource.setImage("https://images.com/image.png");
        return dataSource;
    }

    public static StudyDetailVO createStudy() {
        StudyDetailVO study = new StudyDetailVO();
        study.setStudyDbId("study1");
        study.setUri("Test URI");
        study.setStudyName("Study 1");
        study.setStudyType("Doability");
        study.setProgramName("Program 1");
        study.setActive(true);
        study.setStartDate(new Date());
        study.setDataLinks(Arrays.asList(createDataLink()));
        study.setContacts(Arrays.asList(createContact()));

        BrapiAdditionalInfo additionalInfo = new BrapiAdditionalInfo();
        additionalInfo.addProperty("foo", "bar");
        additionalInfo.addProperty("baz", "zing");
        additionalInfo.addProperty("blob", null);
        study.setAdditionalInfo(additionalInfo);

        study.setLocationDbId("france");
        study.setLocationName("France");
        study.setGermplasmDbIds(Collections.singleton("germplasm1"));
        study.setTrialDbIds(Collections.singleton("trial1"));
        return study;
    }

    private static ContactVO createContact() {
        ContactVO contact = new ContactVO();
        contact.setType("Pro");
        contact.setName("John Doe");
        contact.setEmail("john@doe.com");
        contact.setInstituteName("UCLA");
        return contact;
    }

    private static BrapiStudyDataLink createDataLink() {
        StudyDataLinkVO dataLink = new StudyDataLinkVO();
        dataLink.setName("Link 1");
        dataLink.setUrl("http://inrae.fr");
        return dataLink;
    }

    public static HtmlContentResultMatchers htmlContent() {
        return new HtmlContentResultMatchers();
    }

    public static GermplasmVO createGermplasm() {
        GermplasmVO germplasm = new GermplasmVO();

        germplasm.setGermplasmDbId("germplasm1");
        germplasm.setGermplasmName("BLE BARBU DU ROUSSILLON");
        germplasm.setAccessionNumber("1408");
        germplasm.setSynonyms(Arrays.asList("BLE DU ROUSSILLON", "FRA051:1699", "ROUSSILLON"));
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
        SiteVO originSite = new SiteVO();
        originSite.setSiteId("1234");
        originSite.setSiteName("Le Moulon");
        originSite.setSiteType("Origin site");
        originSite.setLatitude(47.0F);
        originSite.setLongitude(12.0F);
        germplasm.setOriginSite(originSite);

        List<SiteVO> evaluationSites = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SiteVO evaluationSite = new SiteVO();
            evaluationSite.setSiteId(Integer.toString(12347 + i));
            evaluationSite.setSiteType("Evaluation site");
            evaluationSite.setSiteName("Site " + i);
            evaluationSite.setLatitude(46.0F + i);
            evaluationSite.setLongitude(13.0F + i);
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
        germplasm.setTaxonIds(Arrays.asList(createTaxonId(), createTaxonId()));
        germplasm.setTaxonComment("C'est bon le blé");
        germplasm.setTaxonCommonNames(Arrays.asList("Blé tendre", "Bread wheat", "Soft wheat"));
        germplasm.setTaxonSynonyms(Arrays.asList("Blé tendre1", "Bread wheat1", "Soft wheat1"));

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
        collectingSite.setLatitude(48.0F);
        collectingSite.setLongitude(13.0F);
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

        germplasm.setDonors(Arrays.asList(
            createDonor()
        ));

        germplasm.setDistributors(Arrays.asList(
            createDistributor()
        ));

        germplasm.setChildren(Arrays.asList(createChild(), createChild()));

        germplasm.setGermplasmPUI("germplasmPUI");
        germplasm.setPopulation(Arrays.asList(createPopulation1(), createPopulation2(), createPopulation3()));

        germplasm.setCollection(Arrays.asList(createCollection()));

        germplasm.setPanel(Arrays.asList(createPanel()));

        return germplasm;
    }

    public static GermplasmVO createGermplasmMinimal() {
        GermplasmVO germplasm = new GermplasmVO();

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
        germplasm.setTaxonIds(Arrays.asList(createTaxonId(), createTaxonId()));

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

    private static PedigreeVO createPedigree() {
        PedigreeVO result = new PedigreeVO();
        result.setPedigree("Pedigree 1");
        result.setParent1DbId("12345");
        result.setParent1Name("Parent 1");
        result.setParent1Type("P1");
        result.setParent2DbId("12346");
        result.setParent2Name("Parent 2");
        result.setParent2Type("P2");
        result.setCrossingPlan("crossing plan 1");
        result.setCrossingYear("2012");
        result.setSiblings(Arrays.asList(createBrapiSibling()));
        return result;
    }

    private static BrapiSibling createBrapiSibling() {
        SiblingVO sibling = new SiblingVO();
        sibling.setGermplasmDbId("5678");
        sibling.setDefaultDisplayName("Sibling 5678");
        return sibling;
    }

    private static GenealogyVO createChild() {
        GenealogyVO result = new GenealogyVO();
        result.setFirstParentName("CP1");
        result.setSecondParentName("CP2");
        result.setSibblings(Arrays.asList(createPuiNameValueVO(), createPuiNameValueVO()));
        return result;
    }

    private static PuiNameValueVO createPuiNameValueVO() {
        PuiNameValueVO result = new PuiNameValueVO();
        result.setName("Child 1");
        result.setPui("pui1");
        return result;
    }

    private static CollPopVO createPopulation1() {
        CollPopVO result = new CollPopVO();
        result.setName("Population 1");
        result.setType("Pop Type 1");
        result.setGermplasmCount(3);
        result.setGermplasmRef(createPuiNameValueVO());
        return result;
    }

    private static CollPopVO createPopulation2() {
        CollPopVO result = new CollPopVO();
        result.setName("Population 2");
        result.setGermplasmCount(3);
        PuiNameValueVO puiNameValueVO = createPuiNameValueVO();
        puiNameValueVO.setPui("germplasmPUI");
        result.setGermplasmRef(puiNameValueVO);
        return result;
    }

    private static CollPopVO createPopulation3() {
        CollPopVO result = new CollPopVO();
        result.setName("Population 3");
        result.setGermplasmCount(5);
        return result;
    }

    private static CollPopVO createCollection() {
        CollPopVO result = new CollPopVO();
        result.setName("Collection 1");
        result.setGermplasmCount(7);
        return result;
    }

    private static CollPopVO createPanel() {
        CollPopVO result = new CollPopVO();
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

    public static TrialVO createTrial() {
        TrialVO trial = new TrialVO();
        trial.setTrialName("Trail 1");
        trial.setTrialType("Trial type 1");
        trial.setDocumentationURL("http://trials.com");
        trial.setStudies(Arrays.asList(createTrialStudy()));
        return trial;
    }

    private static BrapiTrialStudy createTrialStudy() {
        TrialStudySummaryVO study = new TrialStudySummaryVO();
        study.setStudyDbId("study2");
        study.setStudyName("Study 2");
        return study;
    }

    public static GermplasmAttributeValueListVO createGermplasmAttributeValueList() {
        GermplasmAttributeValueVO value = new GermplasmAttributeValueVO();
        value.setAttributeName("A1");
        value.setValue("V1");

        GermplasmAttributeValueListVO list = new GermplasmAttributeValueListVO();
        list.setData(Arrays.asList(value));
        return list;
    }

    public static ObservationVariableVO createVariable() {
        ObservationVariableVO variable = new ObservationVariableVO();
        variable.setObservationVariableDbId("variable1");
        variable.setDocumentationURL("http://variables.com");
        variable.setName("Variable 1");
        variable.setSynonyms(Arrays.asList("V1"));
        variable.setOntologyName("Ontology 1");
        TraitVO trait = new TraitVO();
        trait.setDescription("Trait 1");
        variable.setTrait(trait);
        return variable;
    }

    public static GermplasmMcpdVO createGermplasmMcpd() {
        GermplasmMcpdVO result = new GermplasmMcpdVO();
        result.setGermplasmPUI("PUI1");
        result.setInstituteCode("Inst1");
        return result;
    }
}
