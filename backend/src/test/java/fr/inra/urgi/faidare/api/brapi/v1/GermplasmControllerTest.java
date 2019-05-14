package fr.inra.urgi.faidare.api.brapi.v1;

import fr.inra.urgi.faidare.domain.data.germplasm.CollPopVO;
import fr.inra.urgi.faidare.domain.data.germplasm.DonorVO;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.germplasm.ProgenyVO;
import fr.inra.urgi.faidare.repository.es.GermplasmAttributeRepository;
import fr.inra.urgi.faidare.service.es.GermplasmService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GermplasmController.class)
class GermplasmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GermplasmService service;

    @MockBean
    private GermplasmAttributeRepository germplasmAttributeRepository;

    static GermplasmVO GERMPLASM;
    static {
        String id = "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NjM4MTc4MzY5NkUxMg==";
        String uri = "http://doi.org/foo/bar";
        GERMPLASM = new GermplasmVO();
        GERMPLASM.setUri(uri);
        GERMPLASM.setGermplasmDbId(id);
    }

    @Test
    void should_Not_Show_JSON_LD_Fields_By_Default() throws Exception {
        when(service.getById(anyString())).thenReturn(GERMPLASM);

        mockMvc.perform(get("/brapi/v1/germplasm/" + GERMPLASM.getGermplasmDbId())
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.@id").doesNotExist());
    }

    @Test
    void should_Show_JSON_LD_Fields_When_Asked() throws Exception {
        when(service.getById(anyString())).thenReturn(GERMPLASM);

        mockMvc.perform(get("/brapi/v1/germplasm/"+GERMPLASM.getGermplasmDbId())
            .accept(BrapiJSONViewHandler.APPLICATION_LD_JSON)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.@id", is(GERMPLASM.getUri())));
    }

    @Test
    void should_Load_Germplasm_progeny_From_PUID() throws Exception {
        ProgenyVO progeny = new ProgenyVO();
        when(service.getProgeny(anyString())).thenReturn(progeny);

        mockMvc.perform(get("/brapi/v1/germplasm/Z25waXNfcHVpOnVua25vd246UmljZToxNjc4MzEw/progeny")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    void should_Load_Germplasm_Encoded_DOI() throws Exception {
        GermplasmVO germplasm = new GermplasmVO();
        when(service.getById(anyString())).thenReturn(germplasm);

        mockMvc.perform(get("/brapi/v1/germplasm/Z25waXNfcHVpOnVua25vd246UmljZToxNjc4MzEw")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    void should_Return_Not_Found() throws Exception {
        when(service.getById(anyString())).thenReturn(null);

        mockMvc.perform(get("/brapi/v1/germplasm/foo")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.metadata.status", hasSize(1)))
            .andExpect(jsonPath("$.metadata.status[0].code", is("404")));
    }


    @Test
    void should_Serialize_Fields_Correctly() throws Exception {
        GermplasmVO germplasm = new GermplasmVO();

        germplasm.setGroupId(0L);
        germplasm.setSpeciesGroup(Collections.singletonList(1L));

        germplasm.setGermplasmDbId("germplasmDbId");
        germplasm.setDefaultDisplayName("defaultDisplayName");

        CollPopVO collection = new CollPopVO();
        collection.setName("name");
        germplasm.setCollection(Collections.singletonList(collection));

        DonorVO donor = new DonorVO();
        donor.setDonorGermplasmPUI("pui");
        donor.setDonationDate(1);
        germplasm.setDonors(Collections.singletonList(donor));

        when(service.getById(anyString())).thenReturn(germplasm);

        mockMvc.perform(get("/brapi/v1/germplasm/foo")
            .contentType(MediaType.APPLICATION_JSON_UTF8))

            // Should not have private fields
            .andExpect(jsonPath("$.result", not(hasProperty("groupId"))))
            .andExpect(jsonPath("$.result", not(hasProperty("speciesGroup"))))

            // BrAPI fields should appear
            .andExpect(jsonPath("$.result.germplasmDbId", is(germplasm.getGermplasmDbId())))
            .andExpect(jsonPath("$.result.defaultDisplayName", is(germplasm.getDefaultDisplayName())))
            .andExpect(jsonPath("$.result.donors[0].donorGermplasmPUI", is(donor.getDonorGermplasmPUI())))

            // GnpIS specific fields should not appear
            .andExpect(jsonPath("$.result.donors[0]", not(hasProperty("donationDate"))))
            .andExpect(jsonPath("$.result", not(hasProperty("collection"))));
    }

}
