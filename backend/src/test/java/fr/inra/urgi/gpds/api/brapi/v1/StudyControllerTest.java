package fr.inra.urgi.gpds.api.brapi.v1;

import fr.inra.urgi.gpds.domain.data.impl.ObservationUnitVO;
import fr.inra.urgi.gpds.domain.data.impl.StudyDetailVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.domain.response.Pagination;
import fr.inra.urgi.gpds.domain.response.PaginationImpl;
import fr.inra.urgi.gpds.repository.es.GermplasmRepository;
import fr.inra.urgi.gpds.repository.es.ObservationUnitRepository;
import fr.inra.urgi.gpds.repository.es.StudyRepository;
import fr.inra.urgi.gpds.repository.file.CropOntologyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StudyController.class)
class StudyControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
	private ObservationUnitRepository observationUnitRepository;

    @MockBean
	private GermplasmRepository germplasmRepository;

    @MockBean
	private CropOntologyRepository cropOntologyRepository;

    @MockBean
	private StudyRepository repository;

	@Test
	void should_Get_By_Id() throws Exception {
		String identifier = "identifier";

		StudyDetailVO study = new StudyDetailVO();
		when(repository.getById(identifier)).thenReturn(study);

		mockMvc.perform(get("/brapi/v1/studies/" + identifier)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	@Test
	void should_Return_Not_Found() throws Exception {
		when(repository.getById("foo")).thenReturn(null);

		mockMvc.perform(get("/brapi/v1/studies/foo")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.metadata.status", hasSize(1)))
				.andExpect(jsonPath("$.metadata.status[0].code", is("404")));
	}

	@Test
	void should_Paginate_ObservationUnits_By_Study() throws Exception {
		String studyDbId = "foo";
		int page = 2;
		int pageSize = 12;

        Pagination pagination = PaginationImpl.create(pageSize, page, 1000);
        PaginatedList<ObservationUnitVO> observationUnits = new PaginatedList<>(pagination, new ArrayList<>());
        when(observationUnitRepository.find(any())).thenReturn(observationUnits);

		mockMvc.perform(get("/brapi/v1/studies/{id}/observationUnits?page={page}&pageSize={pageSize}", studyDbId, page, pageSize)
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.metadata.pagination.currentPage", is(page)))
			.andExpect(jsonPath("$.metadata.pagination.pageSize", is(pageSize)));
	}



}
