package fr.inra.urgi.gpds.api.gnpis.v1;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiResponseFactory;
import fr.inra.urgi.gpds.domain.criteria.DataDiscoveryCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.DataSource;
import fr.inra.urgi.gpds.domain.response.DataDiscoveryResponse;
import fr.inra.urgi.gpds.repository.es.DataDiscoveryRepository;
import fr.inra.urgi.gpds.repository.file.DataSourceRepository;
import fr.inra.urgi.gpds.utils.StringFunctions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(tags = {"GnpIS API", "Data discovery"})
@RestController
@RequestMapping(value= "/gnpis/v1/datadiscovery")
public class DataDiscoveryController {

	private final DataDiscoveryRepository dataDiscoveryRepository;
	private final DataSourceRepository dataSourceRepository;

    @Autowired
    public DataDiscoveryController(DataDiscoveryRepository dataDiscoveryRepository, DataSourceRepository dataSourceRepository) {
        this.dataDiscoveryRepository = dataDiscoveryRepository;
        this.dataSourceRepository = dataSourceRepository;
    }

    @ApiOperation("Suggest data discovery document field values")
	@PostMapping(value = "/suggest", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public Collection<String> suggest(
			@RequestParam String field,
			@RequestParam(required = false) String text,
			@RequestParam(required = false) Long fetchSize,
			@RequestBody(required = false) @Valid DataDiscoveryCriteriaImpl criteria
	) throws UnsupportedEncodingException {
		return dataDiscoveryRepository.suggest(field, StringFunctions.asUTF8(text), fetchSize, criteria);
	}

	@ApiOperation("Search for data discovery documents")
	@PostMapping(value = "/search", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.GnpISAPI.class)
	public DataDiscoveryResponse search(
			@RequestBody @Valid DataDiscoveryCriteriaImpl criteria
	) {
		return dataDiscoveryRepository.find(criteria);
	}

	@ApiOperation("Get list of data sources")
	@GetMapping(value = "/sources", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	@JsonView(JSONView.GnpISAPI.class)
	public BrapiListResponse<? extends DataSource> sources() {
		Collection<DataSource> dataSources = dataSourceRepository.listAll();
		return BrapiResponseFactory.createSubListResponse(dataSources.size(), 0, new ArrayList<>(dataSources));
	}

}
