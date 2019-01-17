package fr.inra.urgi.gpds.api.brapi.v1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CallsController.class)
class CallsControllerTest {

	@Autowired
    private MockMvc mockMvc;

	@Test
	void should_Get_Page_Size() throws Exception {
		int pageSize = 3;
		mockMvc.perform(get("/brapi/v1/calls?pageSize=" + pageSize)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.metadata.status", hasSize(0)))
				.andExpect(jsonPath("$.result.data", hasSize(pageSize)));
	}

	@Test
	void should_Get_All() throws Exception {
		mockMvc.perform(get("/brapi/v1/calls?pageSize=1000")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.metadata.status", hasSize(0)))
				.andExpect(jsonPath("$.result.data[*].datatypes[*]", hasItem("json")))
				.andExpect(jsonPath("$.result.data[*].methods[*]", hasItems("GET", "POST")));
	}

	@Test
	void should_Fail_Page_Overflow() throws Exception {
		mockMvc.perform(get("/brapi/v1/calls?pageSize=50000")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.metadata.status", hasSize(1)))
				.andExpect(jsonPath("$.metadata.status[0].code", is("400")));
	}

}
