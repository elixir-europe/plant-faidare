package fr.inra.urgi.faidare.web.germplasm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmAttributeValue;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiSibling;
import fr.inra.urgi.faidare.domain.criteria.GermplasmAttributeCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmGETSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.CollPopVO;
import fr.inra.urgi.faidare.domain.data.germplasm.DonorVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GenealogyVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmAttributeValueVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmInstituteVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.InstituteVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PhotoVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PuiNameValueVO;
import fr.inra.urgi.faidare.domain.data.germplasm.SiblingVO;
import fr.inra.urgi.faidare.domain.data.germplasm.SimpleVO;
import fr.inra.urgi.faidare.domain.data.germplasm.SiteVO;
import fr.inra.urgi.faidare.domain.data.germplasm.TaxonSourceVO;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.repository.es.GermplasmAttributeRepository;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import fr.inra.urgi.faidare.repository.es.XRefDocumentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller used to display a germplasm card based on its ID.
 * @author JB Nizet
 */
@Controller("webGermplasmController")
@RequestMapping("/germplasms")
public class GermplasmController {

    private final GermplasmRepository germplasmRepository;
    private final FaidareProperties faidareProperties;
    private final XRefDocumentRepository xRefDocumentRepository;
    private GermplasmAttributeRepository germplasmAttributeRepository;

    public GermplasmController(GermplasmRepository germplasmRepository,
                               FaidareProperties faidareProperties,
                               XRefDocumentRepository xRefDocumentRepository,
                               GermplasmAttributeRepository germplasmAttributeRepository) {
        this.germplasmRepository = germplasmRepository;
        this.faidareProperties = faidareProperties;
        this.xRefDocumentRepository = xRefDocumentRepository;
        this.germplasmAttributeRepository = germplasmAttributeRepository;
    }

    @GetMapping("/{germplasmId}")
    public ModelAndView get(@PathVariable("germplasmId") String germplasmId) {
        // GermplasmVO germplasm = germplasmRepository.getById(germplasmId);

        // TODO replace this block by the above commented one
        GermplasmVO germplasm = createGermplasm();

        if (germplasm == null) {
            throw new NotFoundException("Germplasm with ID " + germplasmId + " not found");
        }

        return toModelAndView(germplasm);
    }

    @GetMapping(params = "pui")
    public ModelAndView getByPui(@RequestParam("pui") String pui) {
        GermplasmGETSearchCriteria criteria = new GermplasmGETSearchCriteria();
        criteria.setGermplasmPUI(Collections.singletonList(pui));
        List<GermplasmVO> germplasms = germplasmRepository.find(criteria);
        if (germplasms.size() != 1) {
            throw new NotFoundException("Germplasm with PUI " + pui + " not found");
        }

        return toModelAndView(germplasms.get(0));
    }

    private ModelAndView toModelAndView(GermplasmVO germplasm) {
        // List<BrapiGermplasmAttributeValue> attributes = getAttributes(germplasm);
        // List<XRefDocumentVO> crossReferences = xRefDocumentRepository.find(
        //     XRefDocumentSearchCriteria.forXRefId(site.getLocationDbId()));
        // PedigreeVO pedigree = getPedigree(germplasm);
        // List<XRefDocumentVO> crossReferences = xRefDocumentRepository.find(
        //     XRefDocumentSearchCriteria.forXRefId(germplasm.getGermplasmDbId())
        // );

        // TODO replace this block by the above commented one
        List<BrapiGermplasmAttributeValue> attributes = Arrays.asList(
            createAttribute()
        );
        PedigreeVO pedigree = createPedigree();
        List<XRefDocumentVO> crossReferences = Arrays.asList(
            createXref("foobar"),
            createXref("bazbing")
        );

        sortDonors(germplasm);
        sortPopulations(germplasm);
        sortCollections(germplasm);
        sortPanels(germplasm);
        return new ModelAndView("germplasm",
                                "model",
                                new GermplasmModel(
                                    germplasm,
                                    faidareProperties.getByUri(germplasm.getSourceUri()),
                                    attributes,
                                    pedigree,
                                    crossReferences)
        );
    }

    private void sortPopulations(GermplasmVO germplasm) {
        if (germplasm.getPopulation() != null) {
            germplasm.setPopulation(germplasm.getPopulation()
                                             .stream()
                                             .sorted(Comparator.comparing(
                                                 CollPopVO::getName))
                                             .collect(Collectors.toList()));
        }
    }

    private void sortCollections(GermplasmVO germplasm) {
        if (germplasm.getCollection() != null) {
            germplasm.setCollection(germplasm.getCollection()
                                             .stream()
                                             .sorted(Comparator.comparing(CollPopVO::getName))
                                             .collect(Collectors.toList()));
        }
    }

    private void sortPanels(GermplasmVO germplasm) {
        if (germplasm.getPanel() != null) {
            germplasm.setPanel(germplasm.getPanel()
                                        .stream()
                                        .sorted(Comparator.comparing(CollPopVO::getName))
                                        .collect(Collectors.toList()));
        }
    }

    private void sortDonors(GermplasmVO germplasm) {
        if (germplasm.getDonors() != null) {
            germplasm.setDonors(germplasm.getDonors()
                                         .stream()
                                         .sorted(Comparator.comparing(donor -> donor.getDonorInstitute()
                                                                                    .getInstituteName()))
                                         .collect(Collectors.toList()));
        }
    }

    private List<BrapiGermplasmAttributeValue> getAttributes(GermplasmVO germplasm) {
        GermplasmAttributeCriteria criteria = new GermplasmAttributeCriteria();
        criteria.setGermplasmDbId(germplasm.getGermplasmDbId());
        return germplasmAttributeRepository.find(criteria)
            .stream()
            .flatMap(vo -> vo.getData().stream())
            .sorted(Comparator.comparing(BrapiGermplasmAttributeValue::getAttributeName))
            .collect(Collectors.toList());
    }

    private PedigreeVO getPedigree(GermplasmVO germplasm) {
        return germplasmRepository.findPedigree(germplasm.getGermplasmDbId());
    }

    private BrapiGermplasmAttributeValue createAttribute() {
        GermplasmAttributeValueVO result = new GermplasmAttributeValueVO();
        result.setAttributeName("A1");
        result.setValue("V1");
        return result;
    }

    private GermplasmVO createGermplasm() {
        GermplasmVO result = new GermplasmVO();

        result.setGermplasmName("BLE BARBU DU ROUSSILLON");
        result.setAccessionNumber("1408");
        result.setSynonyms(Arrays.asList("BLE DU ROUSSILLON", "FRA051:1699", "ROUSSILLON"));
        PhotoVO photo = new PhotoVO();
        photo.setPhotoName("Blé du roussillon");
        photo.setCopyright("INRA, Emmanuelle BOULAT/Lionel BARDY 2012");
        photo.setThumbnailFile("https://urgi.versailles.inrae.fr/files/siregal/images/accession/CEREALS/thumbnails/thumb_1408_R09_S.jpg");
        photo.setFile("https://urgi.versailles.inrae.fr/files/siregal/images/accession/CEREALS/1408_R09_S.jpg");
        result.setPhoto(photo);

        InstituteVO holdingGenBank = new InstituteVO();
        holdingGenBank.setLogo("https://urgi.versailles.inra.fr/files/siregal/images/grc/inra_brc_en.png");
        holdingGenBank.setInstituteName("INRA BRC");
        holdingGenBank.setWebSite("http://google.fr");
        result.setHoldingGenbank(holdingGenBank);

        result.setBiologicalStatusOfAccessionCode("Traditional cultivar/landrace ");
        result.setPedigree("LV");
        SiteVO originSite = new SiteVO();
        originSite.setSiteId("1234");
        originSite.setSiteName("Le Moulon");
        originSite.setSiteType("Origin site");
        originSite.setLatitude(47.0F);
        originSite.setLongitude(12.0F);
        result.setOriginSite(originSite);

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
        result.setEvaluationSites(evaluationSites);

        result.setGenus("Genus 1");
        result.setSpecies("Species 1");
        result.setSpeciesAuthority("Species Auth");
        result.setSourceUri("https://urgi.versailles.inrae.fr/gnpis");
        result.setSubtaxa("Subtaxa 1");
        result.setGenusSpeciesSubtaxa("Triticum aestivum subsp. aestivum");
        result.setSubtaxaAuthority("INRAE");
        result.setTaxonIds(Arrays.asList(createTaxonId(), createTaxonId()));
        result.setTaxonComment("C'est bon le blé");
        result.setTaxonCommonNames(Arrays.asList("Blé tendre", "Bread wheat", "Soft wheat"));
        result.setTaxonSynonyms(Arrays.asList("Blé tendre1", "Bread wheat1", "Soft wheat1"));

        InstituteVO holdingInstitute = new InstituteVO();
        holdingInstitute.setInstituteName("GDEC - UMR Génétique, Diversité et Ecophysiologie des Céréales");
        holdingInstitute.setLogo("https://urgi.versailles.inra.fr/files/siregal/images/grc/inra_brc_en.png");
        holdingInstitute.setWebSite("https://google.fr/q=qsdqsdqsdslqlsdnqlsdqlsdlqskdlqdqlsdqsdqsdqd");
        holdingInstitute.setInstituteCode("GDEC");
        holdingInstitute.setInstituteType("Type1");
        holdingInstitute.setAcronym("G.D.E.C");
        holdingInstitute.setAddress("Lyon");
        holdingInstitute.setOrganisation("SAS");
        result.setHoldingInstitute(holdingInstitute);

        result.setPresenceStatus("Maintained");

        GermplasmInstituteVO collector = new GermplasmInstituteVO();
        collector.setMaterialType("Fork");
        collector.setCollectors("Joe, Jack, William, Averell");
        InstituteVO collectingInstitute = new InstituteVO();
        collectingInstitute.setInstituteName("Ninja Squad");
        collector.setInstitute(collectingInstitute);
        collector.setAccessionNumber("567");
        result.setCollector(collector);

        SiteVO collectingSite = new SiteVO();
        collectingSite.setSiteId("1235");
        collectingSite.setSiteName("St Just");
        collectingSite.setSiteType("Collecting site");
        collectingSite.setLatitude(48.0F);
        collectingSite.setLongitude(13.0F);
        result.setCollectingSite(collectingSite);
        result.setAcquisitionDate("In the summer");

        GermplasmInstituteVO breeder = new GermplasmInstituteVO();
        InstituteVO breedingInstitute = new InstituteVO();
        breedingInstitute.setInstituteName("Microsoft");
        breeder.setInstitute(breedingInstitute);
        breeder.setAccessionCreationDate(2015);
        breeder.setAccessionNumber("678");
        breeder.setRegistrationYear(2016);
        breeder.setDeregistrationYear(2019);
        result.setBreeder(breeder);

        result.setDonors(Arrays.asList(
            createDonor()
        ));

        result.setDistributors(Arrays.asList(
            createDistributor()
        ));

        result.setChildren(Arrays.asList(createChild(), createChild()));

        result.setGermplasmPUI("germplasmPUI");
        result.setPopulation(Arrays.asList(createPopulation1(), createPopulation2(), createPopulation3()));

        result.setCollection(Arrays.asList(createCollection()));

        result.setPanel(Arrays.asList(createPanel()));

        return result;
    }

    private DonorVO createDonor() {
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

    private GermplasmInstituteVO createDistributor() {
        GermplasmInstituteVO result = new GermplasmInstituteVO();
        InstituteVO institute = new InstituteVO();
        institute.setInstituteName("Microsoft");
        result.setInstitute(institute);
        result.setAccessionNumber("678");
        result.setDistributionStatus("OK");
        return result;
    }

    private PedigreeVO createPedigree() {
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

    private BrapiSibling createBrapiSibling() {
        SiblingVO sibling = new SiblingVO();
        sibling.setGermplasmDbId("5678");
        sibling.setDefaultDisplayName("Sibling 5678");
        return sibling;
    }

    private GenealogyVO createChild() {
        GenealogyVO result = new GenealogyVO();
        result.setFirstParentName("CP1");
        result.setSecondParentName("CP2");
        result.setSibblings(Arrays.asList(createPuiNameValueVO(), createPuiNameValueVO()));
        return result;
    }

    private PuiNameValueVO createPuiNameValueVO() {
        PuiNameValueVO result = new PuiNameValueVO();
        result.setName("Child 1");
        result.setPui("pui1");
        return result;
    }

    private CollPopVO createPopulation1() {
        CollPopVO result = new CollPopVO();
        result.setName("Population 1");
        result.setType("Pop Type 1");
        result.setGermplasmCount(3);
        result.setGermplasmRef(createPuiNameValueVO());
        return result;
    }

    private CollPopVO createPopulation2() {
        CollPopVO result = new CollPopVO();
        result.setName("Population 2");
        result.setGermplasmCount(3);
        PuiNameValueVO puiNameValueVO = createPuiNameValueVO();
        puiNameValueVO.setPui("germplasmPUI");
        result.setGermplasmRef(puiNameValueVO);
        return result;
    }

    private CollPopVO createPopulation3() {
        CollPopVO result = new CollPopVO();
        result.setName("Population 3");
        result.setGermplasmCount(5);
        return result;
    }

    private CollPopVO createCollection() {
        CollPopVO result = new CollPopVO();
        result.setName("Collection 1");
        result.setGermplasmCount(7);
        return result;
    }

    private CollPopVO createPanel() {
        CollPopVO result = new CollPopVO();
        result.setName("The_panel_1");
        result.setGermplasmCount(2);
        return result;
    }

    private TaxonSourceVO createTaxonId() {
        TaxonSourceVO result = new TaxonSourceVO();
        result.setTaxonId("taxon1");
        result.setSourceName("ThePlantList");
        return result;
    }

    private XRefDocumentVO createXref(String name) {
        XRefDocumentVO xref = new XRefDocumentVO();
        xref.setName(name);
        xref.setDescription("A very large description for the xref " + name + " which has way more than 120 characters bla bla bla bla bla bla bla bla bla bla bla bla");
        xref.setDatabaseName("db_" + name);
        xref.setUrl("https://google.com");
        xref.setEntryType("type " + name);
        return xref;
    }
}
