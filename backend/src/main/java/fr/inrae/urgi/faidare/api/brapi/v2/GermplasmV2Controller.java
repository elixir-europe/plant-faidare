package fr.inrae.urgi.faidare.api.brapi.v2;


import java.io.IOException;

import fr.inrae.urgi.faidare.dao.v2.*;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import fr.inrae.urgi.faidare.utils.GermplasmV2Mapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Tag(name = "Breeding API latest version (V2)", description = "BrAPI full specifications : https://brapi.org/specification")
@RestController
@RequestMapping({"/brapi/v2"})
public class GermplasmV2Controller {

    private final GermplasmV2Dao germplasmDao;

    private final GermplasmMcpdDao germplasmMcpdDao;

    public GermplasmV2Controller(GermplasmV2Dao germplasmDao, GermplasmMcpdDao germplasmMcpdDao) {
        this.germplasmDao = germplasmDao;
        this.germplasmMcpdDao = germplasmMcpdDao;
    }




    @Value("classpath:serverinfo.json")
    Resource serverInfoFile;

    @GetMapping("/serverinfo")
    public @ResponseBody JsonNode serverinfo() throws IOException {


        ObjectMapper mapper = new ObjectMapper();
        JsonNode response = mapper.readTree(serverInfoFile.getInputStream());
        //JsonNode response = mapper.readTree(serverInfoFile.getFile());
        return response;
    }

    @GetMapping("/germplasm")
    public BrapiListResponse<GermplasmV2VO> germplasm(@ModelAttribute GermplasmV2CriteriaGet germplasmCriteria){
        GermplasmV2Criteria criteria = GermplasmV2Mapper.convertGetToPost(germplasmCriteria);
        return germplasmDao.findGermplasmsByCriteria(criteria);
    }

    @PostMapping(value = "/search/germplasm", consumes = "application/json", produces = "application/json")
    public BrapiListResponse<GermplasmV2VO> searchGermplasm(@RequestBody GermplasmV2Criteria germplasmCriteria){

        return germplasmDao.findGermplasmsByCriteria(germplasmCriteria);
    }

    @GetMapping("/germplasm/{germplasmDbId}")
    public BrapiSingleResponse<GermplasmV2VO> byGermplasmDbId(@PathVariable String germplasmDbId) throws Exception {

        GermplasmV2VO gV2Vo = germplasmDao.getByGermplasmDbId(germplasmDbId);
        return BrapiSingleResponse.brapiResponseOf(gV2Vo, Pageable.ofSize(1));
        //return gV2Vo;
    }

}
