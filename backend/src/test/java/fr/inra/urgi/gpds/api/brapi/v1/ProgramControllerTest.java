package fr.inra.urgi.gpds.api.brapi.v1;

import fr.inra.urgi.gpds.domain.data.ProgramVO;
import fr.inra.urgi.gpds.repository.es.ProgramRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProgramController.class)
class ProgramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProgramRepository repository;

    private static ProgramVO PROGRAM;
    static {
        String id = "ZG9pOjEwLjE1NDU0LzEuNDkyMTc4NjM4MTc4MzY5NkUxMg==";
        String uri = "http://doi.org/foo/bar";
        PROGRAM = new ProgramVO();
        PROGRAM.setUri(uri);
        PROGRAM.setProgramDbId(id);
    }

    @Test
    void should_Not_Show_JSON_LD_Fields_By_Default() throws Exception {
        when(repository.getById(PROGRAM.getProgramDbId())).thenReturn(PROGRAM);
        mockMvc.perform(get("/brapi/v1/programs/" + PROGRAM.getProgramDbId())
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.@id").doesNotExist());
    }

    @Test
    void should_Show_JSON_LD_Fields_When_Asked() throws Exception {
        when(repository.getById(PROGRAM.getProgramDbId())).thenReturn(PROGRAM);

        mockMvc.perform(get("/brapi/v1/programs/"+ PROGRAM.getProgramDbId())
            .accept(BrapiJSONViewHandler.APPLICATION_LD_JSON)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result.@id", is(PROGRAM.getUri())));
    }

    @Test
    void should_Get_By_Id() throws Exception {
        String identifier = "identifier";

        ProgramVO program = new ProgramVO();
        when(repository.getById(identifier)).thenReturn(program);

        mockMvc.perform(get("/brapi/v1/programs/" + identifier)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    void should_Return_Not_Found() throws Exception {
        when(repository.getById("foo")).thenReturn(null);

        mockMvc.perform(get("/brapi/v1/programs/foo")
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.metadata.status", hasSize(1)))
            .andExpect(jsonPath("$.metadata.status[0].code", is("404")));
    }

}
