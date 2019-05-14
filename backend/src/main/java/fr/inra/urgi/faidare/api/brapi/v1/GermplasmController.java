package fr.inra.urgi.faidare.api.brapi.v1;

import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasm;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiGermplasmAttributeValueList;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiPedigree;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiProgeny;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.faidare.domain.criteria.GermplasmAttributeCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmGETSearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmPOSTSearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.domain.response.Pagination;
import fr.inra.urgi.faidare.repository.es.GermplasmAttributeRepository;
import fr.inra.urgi.faidare.service.es.GermplasmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(tags = {"Breeding API"}, description = "BrAPI endpoint")
@RestController
public class GermplasmController {

    private final static Logger LOGGER = LoggerFactory.getLogger(GermplasmController.class);

    private final GermplasmService germplasmService;
    private final GermplasmAttributeRepository germplasmAttributeRepository;

    @Autowired
    public GermplasmController(GermplasmService germplasmService, GermplasmAttributeRepository germplasmAttributeRepository) {
        this.germplasmService = germplasmService;
        this.germplasmAttributeRepository = germplasmAttributeRepository;
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Germplasm/GermplasmDetailsByGermplasmDbId.md
     */
    @ApiOperation("Get germplasm by id")
    @GetMapping("/brapi/v1/germplasm/{germplasmDbId}")
    public BrapiResponse<BrapiGermplasm> getGermplasm(@PathVariable String germplasmDbId) {
        LOGGER.debug("germplasmDbId = " + germplasmDbId);
        BrapiGermplasm germplasm = germplasmService.getById(germplasmDbId);
        if (germplasm == null) {
            throw new NotFoundException("Germplasm not found for id '" + germplasmDbId + "'");
        }
        return ApiResponseFactory.createSingleObjectResponse(germplasm, null);
    }

    @ApiOperation("List germplasm")
    @GetMapping("/brapi/v1/germplasm")
    public BrapiListResponse<? extends BrapiGermplasm> listGermplasm(
        @Valid PaginationCriteriaImpl paginationCriteria
    ) {
        GermplasmGETSearchCriteria criteria = new GermplasmGETSearchCriteria();
        criteria.setPageSize(paginationCriteria.getPageSize());
        criteria.setPage(paginationCriteria.getPage());
        return searchGermplasmService(criteria);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Germplasm/GermplasmSearchGET.md
     */
    @ApiOperation("Search germplasm")
    @GetMapping(value = "/brapi/v1/germplasm-search")
    public BrapiListResponse<? extends BrapiGermplasm> searchGermplasm(
        @Valid GermplasmGETSearchCriteria criteria
    ) {
        return searchGermplasmService(criteria);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Germplasm/GermplasmSearchPOST.md
     */
    @ApiOperation("Search germplasm")
    @PostMapping(value = "/brapi/v1/germplasm-search", consumes = APPLICATION_JSON_VALUE)
    public BrapiListResponse<? extends BrapiGermplasm> searchGermplasm(
        @Valid @RequestBody(required = false) GermplasmPOSTSearchCriteria criteria
    ) {
        return searchGermplasmService(criteria);
    }

    private BrapiListResponse<? extends BrapiGermplasm> searchGermplasmService(GermplasmSearchCriteria searchCriteria) {
        PaginatedList<? extends BrapiGermplasm> pager = germplasmService.find(searchCriteria);
        Pagination pagination = pager.getPagination();
        return ApiResponseFactory.createListResponse(pagination, null, pager);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/GermplasmAttributes/GermplasmAttributeValuesByGermplasmDbId.md
     */
    @ApiOperation("List germplasm attributes")
    @GetMapping("/brapi/v1/germplasm/{germplasmDbId}/attributes")
    public BrapiResponse<BrapiGermplasmAttributeValueList> listGermplasmAttributes(
        @PathVariable String germplasmDbId,
        @RequestParam(required = false) List<String> attributeList,
        @Valid PaginationCriteriaImpl paginationCriteria
    ) {
        GermplasmAttributeCriteria listCriteria = new GermplasmAttributeCriteria();
        listCriteria.setAttributeList(attributeList);
        listCriteria.setGermplasmDbId(germplasmDbId);
        listCriteria.setPage(paginationCriteria.getPage());
        listCriteria.setPageSize(paginationCriteria.getPageSize());
        PaginatedList<? extends BrapiGermplasmAttributeValueList> pager = germplasmAttributeRepository.find(listCriteria);
        BrapiGermplasmAttributeValueList attributes = null;
        if (pager.size() == 1) {
            attributes = pager.get(0);
        }
        return ApiResponseFactory.createSingleObjectResponse(attributes, null);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/GermplasmAttributes/GermplasmAttributeValuesByGermplasmDbId.md
     */
    @ApiOperation("Get germplasm pedigree")
    @GetMapping("/brapi/v1/germplasm/{germplasmDbId}/pedigree")
    public BrapiResponse<BrapiPedigree> getPedigree(
        @PathVariable String germplasmDbId
    ) {
        BrapiPedigree pedigree = germplasmService.getPedigree(germplasmDbId);
        return ApiResponseFactory.createSingleObjectResponse(pedigree, null);
    }

    /**
     * @link https://github.com/plantbreeding/API/blob/master/Specification/Germplasm/Germplasm_GermplasmDbId_Progeny_GET.yaml
     */
    @ApiOperation("Get germplasm progeny")
    @GetMapping("/brapi/v1/germplasm/{germplasmDbId}/progeny")
    public BrapiResponse<BrapiProgeny> getProgeny(
        @PathVariable String germplasmDbId
    ) {
        BrapiProgeny progeny = germplasmService.getProgeny(germplasmDbId);
        return ApiResponseFactory.createSingleObjectResponse(progeny, null);
    }
}
