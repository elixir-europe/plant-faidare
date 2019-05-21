package fr.inra.urgi.faidare.api.brapi.v1;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.domain.brapi.v1.data.*;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.faidare.domain.criteria.*;
import fr.inra.urgi.faidare.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.data.phenotype.ObservationUnitVO;
import fr.inra.urgi.faidare.domain.data.study.StudyDetailVO;
import fr.inra.urgi.faidare.domain.data.study.StudySummaryVO;
import fr.inra.urgi.faidare.domain.data.variable.ObservationVariableVO;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.domain.response.Pagination;
import fr.inra.urgi.faidare.repository.es.GermplasmRepository;
import fr.inra.urgi.faidare.repository.es.ObservationUnitRepository;
import fr.inra.urgi.faidare.repository.es.StudyRepository;
import fr.inra.urgi.faidare.repository.file.CropOntologyRepository;
import fr.inra.urgi.faidare.utils.StringFunctions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
    @GetMapping("/brapi/v1/studies/{studyDbId}")
    public BrapiResponse<BrapiStudyDetail> getStudy(@PathVariable String studyDbId) throws Exception {
        studyDbId = StringFunctions.asUTF8(studyDbId);
        StudyDetailVO result = repository.getById(studyDbId);
        if (result == null) {
            throw new NotFoundException("Study not found for id '" + studyDbId + "'");
        }
        return ApiResponseFactory.createSingleObjectResponse(result, null);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/ListStudySummaries.md
     */
    @ApiOperation("List studies")
    @GetMapping("/brapi/v1/studies")
    public BrapiListResponse<? extends BrapiStudySummary> listStudies(@Valid StudySummaryCriteria criteria) {
        if (criteria == null) {
            criteria = new StudySummaryCriteria();
        }
        PaginatedList<StudySummaryVO> result = repository.find(criteria);
        return ApiResponseFactory.createListResponse(result.getPagination(), null, result);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/SearchStudies.md
     */
    @ApiOperation("Search studies")
    @GetMapping(value = "/brapi/v1/studies-search")
    public BrapiListResponse<? extends BrapiStudySummary> searchStudiesGet(@Valid StudySearchCriteria criteria) {
        return listStudies(criteria);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/SearchStudies.md
     */
    @ApiOperation("Search studies")
    @PostMapping(value = "/brapi/v1/studies-search", consumes = APPLICATION_JSON_VALUE)
    public BrapiListResponse<? extends BrapiStudySummary> searchStudiesPost(@RequestBody(required = false) @Valid StudySearchCriteria criteria) {
        return listStudies(criteria);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyObservationVariables.md
     */
    @ApiOperation("List study observation variables")
    @GetMapping(value = {"/brapi/v1/studies/{studyDbId}/observationVariables", "/brapi/v1/studies/{studyDbId}/observationvariables"})
    public BrapiListResponse<? extends BrapiObservationVariable> listStudyVariables(
        @PathVariable String studyDbId, @Valid PaginationCriteriaImpl criteria
    ) throws Exception {
        studyDbId = StringFunctions.asUTF8(studyDbId);
        if (repository.getById(studyDbId) == null) {
            throw new NotFoundException("Study not found for id '" + studyDbId + "'");
        }

        Set<String> variableIds = repository.getVariableIds(studyDbId);
        List<ObservationVariableVO> result = cropOntologyRepository.getVariableByIds(variableIds);

        return ApiResponseFactory.createSubListResponse(
            criteria.getPageSize(), criteria.getPage(),
            result
        );
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/ObservationUnitDetails.md
     */
    @ApiOperation("List study observation units")
    @GetMapping(value = {"/brapi/v1/studies/{studyDbId}/observationUnits", "/brapi/v1/studies/{studyDbId}/observationunits"})
    public BrapiListResponse<? extends BrapiObservationUnit> listStudyObservationUnits(
        @PathVariable String studyDbId, @Valid StudyObservationUnitCriteria criteria
    ) throws Exception {
        studyDbId = StringFunctions.asUTF8(studyDbId);
        ObservationUnitCriteria ouCriteria = new ObservationUnitCriteria();
        ouCriteria.setPage(criteria.getPage());
        ouCriteria.setPageSize(criteria.getPageSize());
        ouCriteria.setStudyDbIds(Sets.newHashSet(studyDbId));
        ouCriteria.setObservationLevel(criteria.getObservationLevel());
        PaginatedList<ObservationUnitVO> result = observationUnitRepository.find(ouCriteria);
        return ApiResponseFactory.createListResponse(result.getPagination(), null, result);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyGermplasmDetails.md
     */
    @ApiOperation("List study germplasm")
    @GetMapping("/brapi/v1/studies/{studyDbId}/germplasm")
    public BrapiListResponse<? extends BrapiGermplasm> listStudyGermplasm(
        @PathVariable String studyDbId, @Valid PaginationCriteriaImpl criteria
    ) throws Exception {
        studyDbId = StringFunctions.asUTF8(studyDbId);
        StudyDetailVO study = (StudyDetailVO) this.getStudy(studyDbId).getResult();

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
        return ApiResponseFactory.createListResponse(pagination, null, pager);
    }
}
