package fr.inra.urgi.gpds.api.brapi.v1;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.api.NotFoundException;
import fr.inra.urgi.gpds.domain.JSONView;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponseFactory;
import fr.inra.urgi.gpds.domain.criteria.GermplasmAttributeCriteria;
import fr.inra.urgi.gpds.domain.criteria.GermplasmGETSearchCriteria;
import fr.inra.urgi.gpds.domain.criteria.GermplasmPOSTSearchCriteria;
import fr.inra.urgi.gpds.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.GermplasmAttributeValueListVO;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.GermplasmVO;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.PedigreeVO;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.ProgenyVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.domain.response.Pagination;
import fr.inra.urgi.gpds.repository.es.GermplasmAttributeRepository;
import fr.inra.urgi.gpds.service.es.GermplasmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Api(tags={"Breeding API"}, description = "BrAPI endpoints")
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
	@ApiOperation(
			value = "Get germplasm by id",
			notes = "Warning: Please do not URL encode DOI identifiers when trying to access germplasm details (will not work on swagger).")
	@RequestMapping(value = "/brapi/v1/germplasm/{germplasmDbId}", method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.BrapiFields.class)
	public BrapiResponse<GermplasmVO> getGermplasm(@PathVariable String germplasmDbId) {
		LOGGER.debug("germplasmDbId = " + germplasmDbId);
		GermplasmVO germplasm = germplasmService.getById(germplasmDbId);
		if (germplasm == null) {
			throw new NotFoundException("Germplasm not found for id '" + germplasmDbId + "'");
		}
		return BrapiResponseFactory.createSingleObjectResponse(germplasm, null);
	}

	/**
	 * @link https://github.com/plantbreeding/API/blob/master/Specification/Germplasm/GermplasmSearchGET.md
	 */
	@ApiOperation("Search germplasm")
	@RequestMapping(value = "/brapi/v1/germplasm-search", method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.BrapiFields.class)
	public BrapiListResponse<GermplasmVO> searchGermplasm(
			@Valid GermplasmGETSearchCriteria criteria
	) {
		return searchGermplasmService(criteria);
	}

	/**
	 * @link https://github.com/plantbreeding/API/blob/master/Specification/Germplasm/GermplasmSearchPOST.md
	 */
	@ApiOperation("Search germplasm")
	@RequestMapping(value = "/brapi/v1/germplasm-search", method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.BrapiFields.class)
	public BrapiListResponse<GermplasmVO> searchGermplasm(
			@Valid @RequestBody(required = false) GermplasmPOSTSearchCriteria criteria
	) {
		return searchGermplasmService(criteria);
	}

	private BrapiListResponse<GermplasmVO> searchGermplasmService(GermplasmSearchCriteria searchCriteria) {
		PaginatedList<GermplasmVO> pager = germplasmService.find(searchCriteria);
		Pagination pagination = pager.getPagination();
		return BrapiResponseFactory.createListResponse(pagination, null, pager);
	}

	/**
	 * @link https://github.com/plantbreeding/API/blob/master/Specification/GermplasmAttributes/GermplasmAttributeValuesByGermplasmDbId.md
	 */
	@ApiOperation("List germplasm attributes")
	@RequestMapping(value = "/brapi/v1/germplasm/{germplasmDbId}/attributes", method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.BrapiFields.class)
	public BrapiResponse<GermplasmAttributeValueListVO> listGermplasmAttributes(
			@PathVariable String germplasmDbId,
			@RequestParam(required = false) List<String> attributeList,
			@Valid PaginationCriteriaImpl paginationCriteria
	) {
		GermplasmAttributeCriteria listCriteria = new GermplasmAttributeCriteria();
		listCriteria.setAttributeList(attributeList);
		listCriteria.setGermplasmDbId(germplasmDbId);
		listCriteria.setPage(paginationCriteria.getPage());
		listCriteria.setPageSize(paginationCriteria.getPageSize());
		PaginatedList<GermplasmAttributeValueListVO> pager = germplasmAttributeRepository.find(listCriteria);
		return BrapiResponseFactory.createSingleObjectResponse(pager.get(0), null);
	}

	/**
	 * @link https://github.com/plantbreeding/API/blob/master/Specification/GermplasmAttributes/GermplasmAttributeValuesByGermplasmDbId.md
	 */
	@ApiOperation("Get germplasm pedigree")
	@RequestMapping(value = "/brapi/v1/germplasm/{germplasmDbId}/pedigree", method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.BrapiFields.class)
	public BrapiResponse<PedigreeVO> getPedigree (
			@PathVariable String germplasmDbId
	) {

		PedigreeVO pedigree = germplasmService.getPedigree(germplasmDbId);
		return BrapiResponseFactory.createSingleObjectResponse(pedigree, null);
	}

	/**
	 * @link https://github.com/plantbreeding/API/blob/master/Specification/Germplasm/Germplasm_GermplasmDbId_Progeny_GET.yaml
	 */
	@ApiOperation("Get germplasm progeny")
	@RequestMapping(value = "/brapi/v1/germplasm/{germplasmDbId}/progeny", method = GET, produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.BrapiFields.class)
	public BrapiResponse<ProgenyVO> getProgeny (
			@PathVariable String germplasmDbId
	) {

		ProgenyVO progeny = germplasmService.getProgeny(germplasmDbId);
		return BrapiResponseFactory.createSingleObjectResponse(progeny, null);
	}
}
