package fr.inra.urgi.faidare.repository.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.inra.urgi.faidare.Application;
import fr.inra.urgi.faidare.domain.criteria.GermplasmGETSearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmPOSTSearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmSitemapVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.PedigreeVO;
import fr.inra.urgi.faidare.domain.data.germplasm.ProgenyVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.domain.response.Pagination;
import fr.inra.urgi.faidare.repository.es.setup.ESSetUp;
import org.assertj.core.data.Index;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmMcpdVO;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@ExtendWith(SpringExtension.class)
@Import({ESSetUp.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("/test.properties")
@SpringBootTest(classes = Application.class)
class GermplasmRepositoryTest {

    @Autowired
    ESSetUp esSetUp;

    @BeforeAll
    void before() {
        esSetUp.initialize(GermplasmVO.class, 0L);
        esSetUp.initialize(ProgenyVO.class, 0L);
        esSetUp.initialize(PedigreeVO.class, 0L);
        esSetUp.initialize(GermplasmMcpdVO.class, 0L);

    }

    @Autowired
    GermplasmRepository repository;

    @Test
    void should_Get_By_Id() {
        String germplasmDbId = "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NjM4NDcyNjA1MkUxMg==";
        GermplasmVO germplasm = repository.getById(germplasmDbId);
        assertThat(germplasm).isNotNull();
        assertThat(germplasm.getGermplasmDbId()).isEqualTo(germplasmDbId);
    }

    @Test
    void should_Find_By_PUI() {
        String germplasmPUI = "doi:10.15454/1.4921786381783696E12";
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        criteria.setGermplasmPUIs(Collections.singletonList(germplasmPUI));

        PaginatedList<GermplasmVO> germplasm = repository.find(criteria);
        assertThat(germplasm).isNotNull().hasSize(1);
        assertThat(germplasm.get(0).getGermplasmPUI()).isEqualTo(germplasmPUI);
    }


    @Test
    void should_Get_Mcpd_By_Id() {
        String germplasmDbId = "13705";
        GermplasmMcpdVO germplasm = repository.getAsMcpdById(germplasmDbId);
        assertThat(germplasm).isNotNull();
        assertThat(germplasm.getGermplasmDbId()).isEqualTo(germplasmDbId);
    }


    @Test
    void should_Not_Get_Mcpd_With_Wrong_Id() {
        String germplasmDbId = "489485184";
        GermplasmMcpdVO germplasm = repository.getAsMcpdById(germplasmDbId);
        assertThat(germplasm).isNull();
    }

    @Test
    void should_Get_Mcpd_By_Id_checkAll() {
        String germplasmDbId = "13705";
        GermplasmMcpdVO germplasm = repository.getAsMcpdById(germplasmDbId);
        assertThat(germplasm).isNotNull();
        assertThat(germplasm.getGermplasmDbId()).isEqualTo(germplasmDbId);
        assertThat(germplasm.getCollectingInfo()).isNotNull();
        assertThat(germplasm.getCollectingInfo().getCollectingSite()).isNotNull();
        assertThat(germplasm.getCollectingInfo().getCollectingSite().getLocationDbId()).isNotNull();
        assertThat(germplasm.getCollectingInfo().getCollectingSite().getLocationDbId()).isEqualTo("dXJuOlVSR0kvbG9jYXRpb24vNDA2MzU=");
        assertThat(germplasm.getCollectingInfo().getCollectingSite().getLocationDbId()).isNotEqualTo("dXJuOlVSR0kvbG9");
    }

    @Test
    void should_Not_Get_By_Incorrect_Id() {
        String germplasmDbId = "FOOO";
        GermplasmVO germplasm = repository.getById(germplasmDbId);
        assertThat(germplasm).isNull();
    }

    @Test
    void should_Scroll_All() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        Iterator<GermplasmVO> list = repository.scrollAll(criteria);
        assertThat(list).isNotNull().toIterable().hasSize(14);
    }

    @Test
    void should_Scroll_Nothing() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        criteria.setAccessionNumbers(Collections.singletonList("FOOOO"));
        Iterator<GermplasmVO> list = repository.scrollAll(criteria);
        assertThat(list).isNotNull().toIterable().hasSize(0);
    }

    @Test
    void shouldScrollAllForSitemap() {
        Iterator<GermplasmSitemapVO> list = repository.scrollAllForSitemap(100);
        assertThat(list).toIterable()
                        .isNotEmpty()
                        .allMatch(vo -> !vo.getGermplasmDbId().isEmpty());
    }

    @Test
    void should_Scroll_By_accessionNumber() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        String accessionNumber = "1801Mtp3";

        criteria.setAccessionNumbers(Collections.singletonList(accessionNumber));

        Iterator<GermplasmVO> list = repository.scrollAll(criteria);
        assertThat(list).isNotNull()
                        .toIterable()
                        .hasSize(1)
                        .extracting("accessionNumber").containsOnly(accessionNumber);
    }

    @Test
    void should_Scroll_Germplasm_By_accessionNumber_Numeric() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        String accessionNumber = "2360";
        criteria.setAccessionNumbers(Collections.singletonList(accessionNumber));
        Iterator<GermplasmVO> list = repository.scrollAll(criteria);
        assertThat(list).isNotNull()
                        .toIterable()
                        .hasSize(1)
                        .extracting("accessionNumber").containsOnly(accessionNumber);
    }

    @Test
    void should_Scroll_Germplasm_By_variety_species() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        String species = "vinifera";
        criteria.setGermplasmSpecies(Lists.newArrayList(species));
        Iterator<GermplasmVO> result = repository.scrollAll(criteria);
        assertThat(result).isNotNull()
                          .toIterable()
                          .isNotEmpty()
                          .extracting("species").containsOnly(species);
    }

    @Test
    void should_Scroll_Germplasm_By_genus() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        String genus = "Solanum";
        criteria.setGermplasmGenus(Lists.newArrayList(genus));
        Iterator<GermplasmVO> g = repository.scrollAll(criteria);
        assertThat(g).isNotNull()
                     .toIterable()
                     .hasSize(2)
                     .extracting("genus").containsOnly(genus);
    }

    @Test
    void should_Find_Paginated_Germplasm_By_Genus_Triticum() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        String genus = "Triticum";
        criteria.setGermplasmGenus(Lists.newArrayList(genus));
        PaginatedList<GermplasmVO> pager = repository.find(criteria);
        assertThat(pager).isNotNull().hasSize(3)
            .extracting("genus").containsOnly(genus);
    }

    @Test
    void should_Find_Paginated_Germplasm_By_Species_TriticumAestivum() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        String species = "aestivum";
        criteria.setGermplasmSpecies(Lists.newArrayList(species));
        PaginatedList<GermplasmVO> pager = repository.find(criteria);
        assertThat(pager).isNotNull().hasSize(3)
            .extracting("species").containsOnly(species);
    }

    @Test
    void should_Find_Paginated_Germplasm_By_Names_CHARGER() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        String name = "CHARGER";
        criteria.setGermplasmNames(Lists.newArrayList(name));
        PaginatedList<GermplasmVO> pager = repository.find(criteria);
        assertThat(pager).isNotNull().hasSize(1).extracting("germplasmName").containsOnly(name);
    }

    @Test
    void should_Find_Paginated_Germplasm_By_Names_CHARGER_AND_Number13431() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        String name = "CHARGER";
        criteria.setGermplasmNames(Lists.newArrayList(name, "13431"));
        PaginatedList<GermplasmVO> pager = repository.find(criteria);
        assertThat(pager).isNotNull().isNotEmpty().extracting("germplasmName").containsOnly(name);
    }

    @Test
    void should_NOT_Find_Paginated_Germplasm_By_Names_dummmy() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        String name = "nobody should name any accession like that";
        criteria.setGermplasmNames(Lists.newArrayList(name));
        PaginatedList<GermplasmVO> pager = repository.find(criteria);
        assertThat(pager).isNotNull().isEmpty();
    }

    @Test
    void should_Find_Paginated_Germplasm_By_Names_dummy_AND_Number13431() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        String name = "CHARGER";
        criteria.setGermplasmNames(Lists.newArrayList(name, "nobody should name any accession like that"));
        PaginatedList<GermplasmVO> pager = repository.find(criteria);
        assertThat(pager).isNotNull().isNotEmpty().extracting("germplasmName").containsOnly(name);
    }

    @Test
    void should_Find_Germplasm_With_Pages_1() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        Long pageSize = 5L;
        criteria.setPage(0L); //Need to test page 0 to check we are 0 based
        criteria.setPageSize(pageSize);
        PaginatedList<GermplasmVO> pager = repository.find(criteria);
        assertThat(pager).isNotNull().isNotEmpty();
        assertThat(pager.size()).isLessThanOrEqualTo(pageSize.intValue());
    }

    @Test
    void should_Find_Germplasm_With_Pages_2() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        Long pageSize = 5L;
        criteria.setPage(0L);
        criteria.setPageSize(pageSize);
        PaginatedList<GermplasmVO> pager = repository.find(criteria);
        assertThat(pager).isNotNull().isNotEmpty();
        assertThat(pager.size()).isLessThanOrEqualTo(pageSize.intValue());
    }

    @Test
    void should_Get_Metadata() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        criteria.setPage(1L);
        criteria.setPageSize(3L);
        PaginatedList<GermplasmVO> g = repository.find(criteria);
        assertThat(g).isNotNull();
    }

    @Test
    void should_Test_Json_Serialization() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        criteria.setPage(1L);
        criteria.setPageSize(3L);
        PaginatedList<GermplasmVO> g = repository.find(criteria);
        assertThat(g).isNotNull();
        String json = null;

        ObjectMapper jacksonMapper = new ObjectMapper();
        try {
            json = jacksonMapper.writeValueAsString(g);
        } catch (JsonProcessingException e) {
            fail("Jackson error", e);
        }
        assertThat(json).isNotNull().isNotEmpty();
    }

    @Test
    void should_Not_Have_Identical_Germplasm_On_Several_Pages() {
        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        criteria.setPage(0L);
        criteria.setPageSize(10L);
        PaginatedList<GermplasmVO> pager0 = repository.find(criteria);
        assertThat(pager0).isNotNull().isNotEmpty();
        assertThat(pager0.getPagination()).isNotNull();

        criteria.setPage(1L);
        PaginatedList<GermplasmVO> pager1 = repository.find(criteria);
        assertThat(pager1).isNotNull().isNotEmpty();
        assertThat(pager0).doesNotContainAnyElementsOf(pager1);
    }

    @Test
    void should_Find_As_Much_Germplasm_Via_Scroll_And_Pager() {
        final String species = "aestivum";
        int numberOfAestivum = 3;

        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        criteria.setGermplasmSpecies(Lists.newArrayList(species));

        Iterator<GermplasmVO> scroll = repository.scrollAll(criteria);
        assertThat(scroll).isNotNull().toIterable().hasSize(numberOfAestivum);

        PaginatedList<GermplasmVO> pager = repository.find(criteria);
        assertThat(pager).isNotNull().isNotEmpty();

        Pagination pagination = pager.getPagination();
        assertThat(pagination).isNotNull();
        Long totalCount = pagination.getTotalCount();
        assertThat(totalCount).isNotNull().isEqualTo(numberOfAestivum);
    }

    @Test
    void should_Succeed_Empty_BrAPI_GET_Search_Criteria() {
        GermplasmSearchCriteria criteria = new GermplasmGETSearchCriteria();
        PaginatedList<GermplasmVO> vos = repository.find(criteria);
        assertThat(vos).isNotNull().isNotEmpty();
        assertThat(vos.size()).isGreaterThan(1);
    }

    @Test
    void should_Succeed_Full_BrAPI_POST_Search_Criteria() {
        List<String> accessionNumbers = Arrays.asList(
            "301Ang6", "P3725", "TX235");
        List<String> germplasmDbIds = Arrays.asList(
            "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NTg2MTk3NTU3OUUxMg==", "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NTcxMTA0NzIwNUUxMg==", "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NjY5NTQ2NjIzM0UxMg==");
        List<String> germplasmNames = Arrays.asList(
            "Sauvignon gris", "Grosse Bleue", "05-HD357.80.");
        List<String> germplasmGenus = Arrays.asList(
            "Vitis", "Prunus", "Triticum");
        List<String> germplasmSpecies = Arrays.asList(
            "vinifera", "domestica", "aestivum");

        GermplasmPOSTSearchCriteria criteria = new GermplasmPOSTSearchCriteria();
        criteria.setAccessionNumbers(accessionNumbers);
        criteria.setGermplasmDbIds(germplasmDbIds);
        criteria.setGermplasmNames(germplasmNames);
        criteria.setGermplasmGenus(germplasmGenus);
        criteria.setGermplasmSpecies(germplasmSpecies);

        PaginatedList<GermplasmVO> vos = repository.find(criteria);
        assertThat(vos).isNotNull().isNotEmpty();

        assertThat(vos).extracting("accessionNumber").isSubsetOf(accessionNumbers);
        assertThat(vos).extracting("germplasmDbId").isSubsetOf(germplasmDbIds);
        assertThat(vos).extracting("germplasmName").isSubsetOf(germplasmNames);
        assertThat(vos).extracting("genus").isSubsetOf(germplasmGenus);
        assertThat(vos).extracting("species").isSubsetOf(germplasmSpecies);
    }

    @Test
    void should_Succeed_Full_BrAPI_GET_Search_Criteria() {
        List<String> germplasmDbIds = Arrays.asList(
            "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NTg2MTk3NTU3OUUxMg==", "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NTcxMTA0NzIwNUUxMg==", "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NjY5NTQ2NjIzM0UxMg==");
        List<String> germplasmNames = Arrays.asList(
            "Sauvignon gris", "Grosse Bleue", "05-HD357.80.");

        GermplasmGETSearchCriteria criteria = new GermplasmGETSearchCriteria();
        criteria.setGermplasmDbId(germplasmDbIds);
        criteria.setGermplasmName(germplasmNames);

        PaginatedList<GermplasmVO> vos = repository.find(criteria);
        assertThat(vos).isNotNull().isNotEmpty();

        assertThat(vos).extracting("germplasmDbId").isSubsetOf(germplasmDbIds);
        assertThat(vos).extracting("germplasmName").isSubsetOf(germplasmNames);
    }

    @Test
    void should_get_empty_progeny_by_id() {
        String germplasmDbId = "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4Njg4NjAyMzEwNUUxMg==";
        ProgenyVO progeny = repository.findProgeny(germplasmDbId);
        assertThat(progeny).isNotNull();
        assertThat(progeny.getGermplasmDbId()).isEqualTo(germplasmDbId);
        assertThat(progeny.getProgeny()).isNotNull().isEmpty();
    }

    @Test
    void should_get_progeny_by_id() {
        String germplasmDbId = "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NTMwMjc4NDQ4MkUxMg==";
        ProgenyVO progeny = repository.findProgeny(germplasmDbId);
        assertThat(progeny).isNotNull();
        assertThat(progeny.getGermplasmDbId()).isEqualTo(germplasmDbId);
        assertThat(progeny.getProgeny()).isNotNull().hasSize(1);
    }

    @Test
    void should_get_pedigree_by_id() {
        String germplasmDbId = "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4Njc4MjQwNzQ2OEUxMg==";
        PedigreeVO pedigree = repository.findPedigree(germplasmDbId);
        assertThat(pedigree).isNotNull();
        assertThat(pedigree.getGermplasmDbId()).isEqualTo(germplasmDbId);
        assertThat(pedigree.getSiblings()).isNotNull().hasSize(9);
    }
}
