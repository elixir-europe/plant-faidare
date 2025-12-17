package fr.inrae.urgi.faidare.api.brapi.v1;

import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.api.brapi.v2.BrapiResponse;
import fr.inrae.urgi.faidare.api.brapi.v2.BrapiSingleResponse;
import fr.inrae.urgi.faidare.dao.file.CropOntologyRepository;
import fr.inrae.urgi.faidare.domain.variable.BrapiObservationVariable;
import fr.inrae.urgi.faidare.domain.variable.ObservationVariableV1VO;
import fr.inrae.urgi.faidare.domain.variable.OntologyVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(name = "Breeding API V1", description = "Deprecated version, use latest as much as possible")
@RestController
public class ObservationVariableV1Controller {

    private final CropOntologyRepository cropOntologyRepository;


    public ObservationVariableV1Controller(CropOntologyRepository cropOntologyRepository) {
        this.cropOntologyRepository = cropOntologyRepository;
    }
    @GetMapping("/brapi/v1/variables/{observationVariableDbId}")
    public BrapiResponse<BrapiObservationVariable> getVariable(@PathVariable String observationVariableDbId) throws Exception {
        ObservationVariableV1VO observationVariableV1VO = cropOntologyRepository.getVariableById(observationVariableDbId);
        if (observationVariableV1VO == null) {
            throw new IllegalArgumentException("Observation variable with id " + observationVariableDbId + " not found");
        }
        return BrapiSingleResponse.brapiResponseOf(observationVariableV1VO, Pageable.ofSize(1));
    }

    @GetMapping("/brapi/v1/ontologies")
    public BrapiResponse<OntologyVO> getOntologies(@RequestParam(required = false, defaultValue = "0") @Nullable int page,
                                                   @RequestParam(required = false, defaultValue = "1000") @Nullable int pageSize) {
        List<OntologyVO> ontologies = cropOntologyRepository.getOntologies();
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        return BrapiListResponse.brapiResponseForPageOf(ontologies, pageable, ontologies.size());
    }

    @GetMapping("/brapi/v1/variables")
    public BrapiResponse<ObservationVariableV1VO> getVariables(@RequestParam(required = false) String traitClass,
                                                               @RequestParam(required = false, defaultValue = "0") @Nullable int page,
                                                               @RequestParam(required = false, defaultValue = "10") @Nullable int pageSize) {

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        if (traitClass != null && !traitClass.isEmpty()) {
            traitClass = URLDecoder.decode(traitClass, StandardCharsets.UTF_8);
            List<ObservationVariableV1VO> variables = cropOntologyRepository.getVariablesByTraitClass(traitClass);
            if (variables == null || variables.isEmpty()) {
                throw new IllegalArgumentException("No variables found for trait class: " + traitClass);
            }
            Integer variablesCount = cropOntologyRepository.getVariablesByTraitClassCount(traitClass);
            return BrapiListResponse.brapiResponseForPageOf(variables, pageable, variablesCount);
        }else {
            List<ObservationVariableV1VO> variables = cropOntologyRepository.getVariables();
            Integer variablesCount = cropOntologyRepository.getVariablesCount();
            return BrapiListResponse.brapiResponseForPageOf(variables, pageable, variablesCount);
        }
    }



}
