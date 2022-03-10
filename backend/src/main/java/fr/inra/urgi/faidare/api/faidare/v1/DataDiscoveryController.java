package fr.inra.urgi.faidare.api.faidare.v1;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import javax.validation.Valid;

import fr.inra.urgi.faidare.config.FaidareProperties;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.datadiscovery.criteria.DataDiscoveryCriteriaImpl;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSource;
import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSourceImpl;
import fr.inra.urgi.faidare.domain.datadiscovery.response.DataDiscoveryResponse;
import fr.inra.urgi.faidare.domain.response.ApiResponseFactory;
import fr.inra.urgi.faidare.repository.es.DataDiscoveryRepository;
import fr.inra.urgi.faidare.utils.StringFunctions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAIDARE API", description = "Extended FAIDARE API")
@RestController
@RequestMapping(value = "/faidare/v1/datadiscovery")
public class DataDiscoveryController {

    private final DataDiscoveryRepository dataDiscoveryRepository;
    private final FaidareProperties properties;

    @Autowired
    public DataDiscoveryController(DataDiscoveryRepository dataDiscoveryRepository, FaidareProperties properties) {
        this.dataDiscoveryRepository = dataDiscoveryRepository;
        this.properties = properties;
    }

    @Operation(summary = "Suggest data discovery document field values")
    @PostMapping("/suggest")
    public Collection<String> suggest(
        @RequestParam String field,
        @RequestParam(required = false) String text,
        @RequestParam(required = false) Integer fetchSize,
        @RequestBody(required = false) @Valid DataDiscoveryCriteriaImpl criteria
    ) throws UnsupportedEncodingException {
        if (fetchSize == null) {
            fetchSize = Integer.MAX_VALUE;
        }
        return dataDiscoveryRepository.suggest(field, StringFunctions.asUTF8(text), fetchSize, criteria);
    }

    @Operation(summary = "Search for data discovery documents")
    @PostMapping(value = "/search", consumes = APPLICATION_JSON_VALUE)
    public DataDiscoveryResponse search(
        @RequestBody @Valid DataDiscoveryCriteriaImpl criteria
    ) {
        return dataDiscoveryRepository.find(criteria);
    }

    @Operation(summary = "Get list of data sources")
    @GetMapping("/sources")
    public BrapiListResponse<? extends DataSource> sources() {
        List<DataSourceImpl> dataSources = properties.getDataSources();
        return ApiResponseFactory.createSubListResponse(dataSources.size(), 0, dataSources);
    }

}
