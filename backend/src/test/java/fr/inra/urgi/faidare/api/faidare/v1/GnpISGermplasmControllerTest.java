package fr.inra.urgi.faidare.api.faidare.v1;

import fr.inra.urgi.faidare.domain.criteria.GermplasmGETSearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.CollPopVO;
import fr.inra.urgi.faidare.domain.data.germplasm.DonorVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.service.es.GermplasmService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author C. Michotey, E. Kimmel, gcornut
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GnpISGermplasmController.class)
class GnpISGermplasmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GermplasmService service;

    @Test
    void should_Return_Not_Found_With_Id() throws Exception {
        String id = "foo";
        when(service.getById(id)).thenReturn(null);

        mockMvc.perform(get("/brapi/v1/germplasm?id=" + id)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound());
    }

    @Test
    void should_Return_Not_Found_With_PUI() throws Exception {
        when(service.find(any(GermplasmSearchCriteria.class))).thenReturn(null);

        String pui = "foo";
        mockMvc.perform(get("/brapi/v1/germplasm?pui=" + pui)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound());
    }

    @Test
    void should_Load_Germplasm_From_DOI() throws Exception {
        GermplasmVO germplasm = new GermplasmVO();
        PaginatedList<GermplasmVO> germplasmPage = new PaginatedList<>(null, Collections.singletonList(germplasm));

        ArgumentCaptor<GermplasmSearchCriteria> criteriaCaptor = ArgumentCaptor.forClass(GermplasmSearchCriteria.class);
        when(service.find(criteriaCaptor.capture())).thenReturn(germplasmPage);

        String pui = "doi:10.15454/1.4921786234137117E12";
        mockMvc.perform(get("/faidare/v1/germplasm?pui=" + pui)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        GermplasmSearchCriteria value = criteriaCaptor.getValue();
        assertThat(value).isInstanceOf(GermplasmGETSearchCriteria.class);

        // Check the generated criteria contains only the PUI given in REST query param
        GermplasmGETSearchCriteria criteria = (GermplasmGETSearchCriteria) value;
        assertThat(criteria.getGermplasmPUI()).isNotNull().hasSize(1).containsOnly(pui);
    }

    @Test
    void should_Return_Bad_Request_With_No_Param() throws Exception {
        mockMvc.perform(get("/faidare/v1/germplasm")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should_Serialize_Fields_Correctly() throws Exception {
        GermplasmVO germplasm = new GermplasmVO();

        germplasm.setGroupId(0L);

        germplasm.setGermplasmDbId("germplasmDbId");
        germplasm.setDefaultDisplayName("defaultDisplayName");

        // Add GnpIS specific field
        CollPopVO collection = new CollPopVO();
        collection.setName("name");
        germplasm.setCollection(Collections.singletonList(collection));

        DonorVO donor = new DonorVO();
        donor.setDonorGermplasmPUI("pui");
        germplasm.setDonors(Collections.singletonList(donor));

        PaginatedList<GermplasmVO> germplasmPage = new PaginatedList<>(null, Collections.singletonList(germplasm));

        when(service.find(any(GermplasmSearchCriteria.class))).thenReturn(germplasmPage);

        mockMvc.perform(get("/faidare/v1/germplasm?pui=foo")
            .contentType(MediaType.APPLICATION_JSON_UTF8))

            // Should not have private fields
            .andExpect(jsonPath("$", not(hasProperty("groupId"))))
            .andExpect(jsonPath("$", not(hasProperty("speciesGroup"))))

            // BrAPI fields should appear
            .andExpect(jsonPath("$.germplasmDbId", is(germplasm.getGermplasmDbId())))
            .andExpect(jsonPath("$.defaultDisplayName", is(germplasm.getDefaultDisplayName())))
            .andExpect(jsonPath("$.donors[0].donorGermplasmPUI", is(donor.getDonorGermplasmPUI())))

            // GnpIS specific fields should appear
            //.andExpect(jsonPath("$.donors[0].donationDate", is(donor.getDonationDate())))
            .andExpect(jsonPath("$.collection[0].name", is(collection.getName())));

    }

}
