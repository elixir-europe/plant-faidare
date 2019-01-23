package fr.inra.urgi.gpds.api.brapi.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fr.inra.urgi.gpds.api.NotFoundException;
import fr.inra.urgi.gpds.domain.JSONView;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponseFactory;
import fr.inra.urgi.gpds.domain.criteria.*;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.impl.ObservationUnitVO;
import fr.inra.urgi.gpds.domain.data.impl.StudyDetailVO;
import fr.inra.urgi.gpds.domain.data.impl.StudySummaryVO;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.GermplasmVO;
import fr.inra.urgi.gpds.domain.data.impl.variable.ObservationVariableVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.domain.response.Pagination;
import fr.inra.urgi.gpds.repository.es.GermplasmRepository;
import fr.inra.urgi.gpds.repository.es.ObservationUnitRepository;
import fr.inra.urgi.gpds.repository.es.StudyRepository;
import fr.inra.urgi.gpds.repository.file.CropOntologyRepository;
import fr.inra.urgi.gpds.utils.StringFunctions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author gcornut
 */
@Api(tags = {"Breeding API"}, description = "BrAPI endpoint")
@RestController
public class StudyController {

    private final StudyRepository repository;
    private final ObservationUnitRepository observationUnitRepository;
    private final GermplasmRepository germplasmRepository;
    private final CropOntologyRepository cropOntologyRepository;

    @Autowired
    public StudyController(
        StudyRepository repository,
        ObservationUnitRepository observationUnitRepository,
        GermplasmRepository germplasmRepository,
        CropOntologyRepository cropOntologyRepository
    ) {
        this.repository = repository;
        this.observationUnitRepository = observationUnitRepository;
        this.germplasmRepository = germplasmRepository;
        this.cropOntologyRepository = cropOntologyRepository;
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyDetails.md
     */
    @ApiOperation("Get study")
    @RequestMapping(value = "/brapi/v1/studies/{studyDbId}", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(JSONView.BrapiFields.class)
    public BrapiResponse<StudyDetailVO> getStudy(@PathVariable String studyDbId) throws Exception {
        studyDbId = StringFunctions.asUTF8(studyDbId);
        StudyDetailVO result = repository.getById(studyDbId);
        if (result == null) {
            throw new NotFoundException("Study not found for id '" + studyDbId + "'");
        }
        return BrapiResponseFactory.createSingleObjectResponse(result, null);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/ListStudySummaries.md
     */
    @ApiOperation("List studies")
    @RequestMapping(value = "/brapi/v1/studies", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(JSONView.BrapiFields.class)
    public BrapiListResponse<StudySummaryVO> listStudies(@Valid StudySummaryCriteria criteria) {
        if (criteria == null) {
            criteria = new StudySummaryCriteria();
        }
        PaginatedList<StudySummaryVO> result = repository.find(criteria);
        return BrapiResponseFactory.createListResponse(result.getPagination(), null, result);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/SearchStudies.md
     */
    @ApiOperation("Search studies")
    @RequestMapping(value = "/brapi/v1/studies-search", method = {GET, POST}, produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(JSONView.BrapiFields.class)
    public BrapiListResponse<StudySummaryVO> searchStudies(@RequestBody(required = false) @Valid StudySearchCriteria criteria) {
        return listStudies(criteria);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyObservationVariables.md
     */
    @ApiOperation("List study observation variables")
    @RequestMapping(value = "/brapi/v1/studies/{studyDbId}/observationVariables", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(JSONView.BrapiFields.class)
    public BrapiListResponse<ObservationVariableVO> listStudyVariables(
        @PathVariable String studyDbId, @Valid PaginationCriteriaImpl criteria
    ) throws Exception {
        studyDbId = StringFunctions.asUTF8(studyDbId);
        if (repository.getById(studyDbId) == null) {
            throw new NotFoundException("Study not found for id '" + studyDbId + "'");
        }

        Set<String> variableIds = repository.getVariableIds(studyDbId);
        List<ObservationVariableVO> result = cropOntologyRepository.getVariableByIds(variableIds);

        return BrapiResponseFactory.createSubListResponse(
            criteria.getPageSize(), criteria.getPage(),
            result
        );
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/ObservationUnitDetails.md
     */
    @ApiOperation("List study observation units")
    @RequestMapping(value = "/brapi/v1/studies/{studyDbId}/observationUnits", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(JSONView.BrapiFields.class)
    public BrapiListResponse<ObservationUnitVO> listStudyObservationUnits(
        @PathVariable String studyDbId, @Valid StudyObservationUnitCriteria criteria
    ) throws Exception {
        studyDbId = StringFunctions.asUTF8(studyDbId);
        ObservationUnitCriteria ouCriteria = new ObservationUnitCriteria();
        ouCriteria.setPage(criteria.getPage());
        ouCriteria.setPageSize(criteria.getPageSize());
        ouCriteria.setStudyDbIds(Sets.newHashSet(studyDbId));
        ouCriteria.setObservationLevel(criteria.getObservationLevel());
        PaginatedList<ObservationUnitVO> result = observationUnitRepository.find(ouCriteria);
        return BrapiResponseFactory.createListResponse(result.getPagination(), null, result);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyGermplasmDetails.md
     */
    @ApiOperation("List study germplasm")
    @RequestMapping(value = "/brapi/v1/studies/{studyDbId}/germplasm", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    @JsonView(JSONView.BrapiFields.class)
    public BrapiListResponse<GermplasmVO> listStudyGermplasm(
        @PathVariable String studyDbId, @Valid PaginationCriteriaImpl criteria
    ) throws Exception {
        studyDbId = StringFunctions.asUTF8(studyDbId);
        StudyDetailVO study = this.getStudy(studyDbId).getResult();

        PaginatedList<GermplasmVO> pager;
        if (CollectionUtils.isEmpty(study.getGermplasmDbIds())) {
            pager = PaginatedList.createEmptyPaginatedList(criteria);
        } else {
            GermplasmPOSTSearchCriteria germplasmCriteria = new GermplasmPOSTSearchCriteria();
            germplasmCriteria.setPage(criteria.getPage());
            germplasmCriteria.setPageSize(criteria.getPageSize());
            germplasmCriteria.setGermplasmDbIds(Lists.newArrayList(study.getGermplasmDbIds()));

            pager = germplasmRepository.find(germplasmCriteria);
        }
        Pagination pagination = pager.getPagination();
        return BrapiResponseFactory.createListResponse(pagination, null, pager);
    }
}
