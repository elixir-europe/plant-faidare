package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.Application;
import fr.inra.urgi.faidare.domain.datadiscovery.criteria.DataDiscoveryCriteriaImpl;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataDiscoveryDocument;
import fr.inra.urgi.faidare.domain.datadiscovery.data.Facet;
import fr.inra.urgi.faidare.domain.datadiscovery.data.FacetTerm;
import fr.inra.urgi.faidare.domain.datadiscovery.response.DataDiscoveryResponse;
import fr.inra.urgi.faidare.repository.es.setup.ESSetUp;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@Import({ESSetUp.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/test.properties")
@SpringBootTest(classes = Application.class)
class DataDiscoveryRepositoryTest {

    @Autowired
    DataDiscoveryRepositoryImpl repository;

    @Autowired
    ESSetUp esSetUp;

    @BeforeAll
    void before() {
        esSetUp.initialize(DataDiscoveryDocument.class, 0L);
    }

    @Test
    void should_Suggest() {
        String field = "crops";
        Collection<String> result = repository.suggest(field, null, null, null);
        assertThat(result).isNotNull();
        assertThat(result).contains("Triticum");
    }

    @Test
    void should_Suggest_Fetch_Size() {
        String field = "crops";
        Integer fetchSize = 5;
        Collection<String> result = repository.suggest(field, null, fetchSize, null);
        assertThat(result).isNotNull().hasSize(fetchSize);
    }

    @Test
    void should_Suggest_Text() {
        String field = "crops";
        Collection<String> result = repository.suggest(field, "ble", null, null);
        assertThat(result).isNotNull();
        assertThat(result.iterator().next()).contains("Blé");
    }

    @Test
    void should_Suggest_Criteria() {
        String field = "accessions";
        DataDiscoveryCriteriaImpl criteria = new DataDiscoveryCriteriaImpl();
        criteria.setCrops(Arrays.asList("Triticum", "Zea", "Rice"));
        Collection<String> result = repository.suggest(field, null, null, criteria);
        assertThat(result).isNotNull();
        assertThat(result).doesNotContain("V1", "V2");
        assertThat(result).contains("B1", "B2");
    }

    @Test
    void should_Suggest_Ignoring_Criteria_If_Same_Field_As_Suggested() {
        String field = "crops";
        DataDiscoveryCriteriaImpl criteria = new DataDiscoveryCriteriaImpl();
        criteria.setCrops(Arrays.asList("Zea"));
        Collection<String> result = repository.suggest(field, "Triticum", null, criteria);
        assertThat(result).isNotNull();
        assertThat(result).contains("Triticum");
    }

    @Test
    void should_Find_Without_Facets() {
        DataDiscoveryCriteriaImpl criteria = new DataDiscoveryCriteriaImpl();
        DataDiscoveryResponse result = repository.find(criteria);
        assertThat(result.getResult().getData()).isNotNull().isNotEmpty();
        assertThat(result.getFacets()).isNull();
    }

    @Test
    void should_Find_By_Accession() {
        DataDiscoveryCriteriaImpl criteria = new DataDiscoveryCriteriaImpl();
        criteria.setAccessions(Arrays.asList("kolben"));
        DataDiscoveryResponse result = repository.find(criteria);
        assertThat(result.getResult().getData()).isNotNull().isNotEmpty()
            .extracting("name").contains("AUSTRO KOLBEN");
    }

    @Test
    void should_Find_By_Type() {
        DataDiscoveryCriteriaImpl criteria = new DataDiscoveryCriteriaImpl();
        List<String> types = Arrays.asList("Germplasm");
        criteria.setTypes(types);
        DataDiscoveryResponse result = repository.find(criteria);
        assertThat(result.getResult().getData()).isNotNull().hasSize(2)
            .flatExtracting("type").isSubsetOf(types);
    }

    @Test
    void should_Find_By_Source() {
        DataDiscoveryCriteriaImpl criteria = new DataDiscoveryCriteriaImpl();
        List<String> sources = Arrays.asList("http://example.com/catalog1");
        criteria.setSources(sources);
        DataDiscoveryResponse result = repository.find(criteria);
        assertThat(result.getResult().getData()).isNotNull().hasSize(2)
            .extracting("sourceUri").isSubsetOf(sources);
    }

    @Test
    void should_Find_Documents_With_URL() {
        DataDiscoveryCriteriaImpl criteria = new DataDiscoveryCriteriaImpl();
        criteria.setCrops(Arrays.asList("Rice"));
        DataDiscoveryResponse result = repository.find(criteria);
        assertThat(result.getResult().getData()).isNotNull().isNotEmpty().extracting("url").isNotNull().isNotEmpty();
    }

    @Test
    void should_Find_Documents_By_Page() {
        DataDiscoveryCriteriaImpl criteria = new DataDiscoveryCriteriaImpl();
        criteria.setPage(0L);
        criteria.setPageSize(2L);
        criteria.setSortBy("@id");
        criteria.setSortOrder(SortOrder.DESC.toString());
        DataDiscoveryResponse result = repository.find(criteria);
        assertThat(result.getResult().getData().size()).isEqualTo(2);
        assertThat(result.getResult().getData().get(0).getUri()).isEqualTo("urn:germplasm/austro_kolben");
        assertThat(result.getResult().getData().get(1).getUri()).isEqualTo("urn:foo_study");

        assertThat(result.getMetadata().getPagination().getCurrentPage()).isEqualTo(0L);
        assertThat(result.getMetadata().getPagination().getTotalPages()).isEqualTo(5L);
        assertThat(result.getMetadata().getPagination().getPageSize()).isEqualTo(2L);
        assertThat(result.getMetadata().getPagination().getTotalCount()).isEqualTo(10L);

        criteria.setPage(1L);
        criteria.setPageSize(3L);
        result = repository.find(criteria);
        assertThat(result.getResult().getData().size()).isEqualTo(3);
        assertThat(result.getResult().getData().get(0).getUri()).isEqualTo("urn:foo2_germplasm");

        assertThat(result.getMetadata().getPagination().getCurrentPage()).isEqualTo(1L);
        assertThat(result.getMetadata().getPagination().getTotalPages()).isEqualTo(4L);
        assertThat(result.getMetadata().getPagination().getPageSize()).isEqualTo(3L);
        assertThat(result.getMetadata().getPagination().getTotalCount()).isEqualTo(10L);
    }

    @Test
    void should_Find_With_Facets() {
        DataDiscoveryCriteriaImpl criteria = new DataDiscoveryCriteriaImpl();
        String criterionName = "sources";
        criteria.setFacetFields(Arrays.asList(criterionName));
        DataDiscoveryResponse result = repository.find(criteria);
        assertThat(result.getResult().getData()).isNotNull().isNotEmpty();
        assertThat(result.getFacets()).isNotNull().hasSize(1);

        Facet facet = result.getFacets().get(0);
        assertThat(facet.getField()).isEqualTo(criterionName);

        List<? extends FacetTerm> terms = facet.getTerms();
        assertThat(terms).isNotNull().hasSize(2);

        FacetTerm facetTerm = terms.get(0);
        assertThat(facetTerm.getTerm()).isEqualTo("http://example.com/catalog1");
        assertThat(facetTerm.getCount()).isEqualTo(2);

        FacetTerm facetTerm1 = terms.get(1);
        assertThat(facetTerm1.getTerm()).isEqualTo("http://example.com/catalog2");
        assertThat(facetTerm1.getCount()).isEqualTo(1);
    }

    @Test
    void should_Find_With_Filtered_Facets() {
        DataDiscoveryCriteriaImpl criteria = new DataDiscoveryCriteriaImpl();
        String criterionName = "sources";
        criteria.setFacetFields(Arrays.asList(criterionName));
        criteria.setCrops(Arrays.asList(
            "Triticum"
        ));
        DataDiscoveryResponse result = repository.find(criteria);
        assertThat(result.getResult().getData()).isNotNull().isNotEmpty();
        assertThat(result.getFacets()).isNotNull().hasSize(1);

        Facet facet = result.getFacets().get(0);
        assertThat(facet.getField()).isEqualTo(criterionName);

        List<? extends FacetTerm> terms = facet.getTerms();
        assertThat(terms).isNotNull();
        assertThat(terms).isNotNull().hasSize(1);

        FacetTerm facetTerm = terms.get(0);
        assertThat(facetTerm.getTerm()).isEqualTo("http://example.com/catalog2");
        assertThat(facetTerm.getCount()).isEqualTo(1);
    }
}
