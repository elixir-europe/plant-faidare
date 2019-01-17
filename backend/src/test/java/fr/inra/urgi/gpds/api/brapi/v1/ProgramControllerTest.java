package fr.inra.urgi.gpds.api.brapi.v1;

import fr.inra.urgi.gpds.domain.data.impl.ProgramVO;
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
