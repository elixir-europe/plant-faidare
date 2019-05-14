package fr.inra.urgi.faidare.api.gnpis.v1;

import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.datadiscovery.criteria.DataDiscoveryCriteriaImpl;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSource;
import fr.inra.urgi.faidare.domain.datadiscovery.response.DataDiscoveryResponse;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import fr.inra.urgi.faidare.repository.es.DataDiscoveryRepository;
import fr.inra.urgi.faidare.repository.file.DataSourceRepository;
import fr.inra.urgi.faidare.utils.StringFunctions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Api(tags = {"GnpIS API"}, description = "Extended GnpIS API")
@RestController
@RequestMapping(value = "/gnpis/v1/datadiscovery")
public class DataDiscoveryController {

    private final DataDiscoveryRepository dataDiscoveryRepository;
    private final DataSourceRepository dataSourceRepository;

    @Autowired
    public DataDiscoveryController(DataDiscoveryRepository dataDiscoveryRepository, DataSourceRepository dataSourceRepository) {
        this.dataDiscoveryRepository = dataDiscoveryRepository;
        this.dataSourceRepository = dataSourceRepository;
    }

    @ApiOperation("Suggest data discovery document field values")
    @PostMapping("/suggest")
    public Collection<String> suggest(
        @RequestParam String field,
        @RequestParam(required = false) String text,
        @RequestParam(required = false) Long fetchSize,
        @RequestBody(required = false) @Valid DataDiscoveryCriteriaImpl criteria
    ) throws UnsupportedEncodingException {
        return dataDiscoveryRepository.suggest(field, StringFunctions.asUTF8(text), fetchSize, criteria);
    }

    @ApiOperation("Search for data discovery documents")
    @PostMapping(value = "/search", consumes = APPLICATION_JSON_VALUE)
    public DataDiscoveryResponse search(
        @RequestBody @Valid DataDiscoveryCriteriaImpl criteria
    ) {
        return dataDiscoveryRepository.find(criteria);
    }

    @ApiOperation("Get list of data sources")
    @GetMapping("/sources")
    public BrapiListResponse<? extends DataSource> sources() {
        Collection<DataSource> dataSources = dataSourceRepository.listAll();
        return ApiResponseFactory.createSubListResponse(dataSources.size(), 0, new ArrayList<>(dataSources));
    }

}
