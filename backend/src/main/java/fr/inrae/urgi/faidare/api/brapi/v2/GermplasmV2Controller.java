package fr.inrae.urgi.faidare.api.brapi.v2;


import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.inrae.urgi.faidare.dao.v2.CollectionV2Dao;
import fr.inrae.urgi.faidare.dao.v2.GermplasmV2Criteria;
import fr.inrae.urgi.faidare.dao.v2.GermplasmMcpdDao;
import fr.inrae.urgi.faidare.dao.v2.GermplasmV2Dao;
import fr.inrae.urgi.faidare.domain.CollPopVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Breeding API latest version (V2)", description = "BrAPI full specifications : https://brapi.org/specification")
@RestController
@RequestMapping({"/brapi/v2"})
public class GermplasmV2Controller {

    private final GermplasmV2Dao germplasmDao;

    private final CollectionV2Dao collectionDao;

    private final GermplasmMcpdDao germplasmMcpdDao;

    public GermplasmV2Controller(GermplasmV2Dao germplasmDao, CollectionV2Dao collectionDao, GermplasmMcpdDao germplasmMcpdDao) {
        this.germplasmDao = germplasmDao;
        this.collectionDao = collectionDao;
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
    public BrapiListResponse<GermplasmV2VO> germplasm(
        @ModelAttribute GermplasmV2Criteria gCrit){
        // We used @ModelAttribute because GermplasmCriteria is an object.
        // This allows Spring to bind the request parameters directly to the fields of the GermplasmCriteria object.

        BrapiListResponse<GermplasmV2VO> germplasmV2VOs = germplasmDao.findGermplasmsByCriteria(gCrit);

        return germplasmV2VOs;
    }

    @PostMapping(value = "/search/germplasm", consumes = "application/json", produces = "application/json")
    public BrapiListResponse<GermplasmV2VO> searchGermplasm(@RequestBody GermplasmV2Criteria gCrit){

        return germplasmDao.findGermplasmsByCriteria(gCrit);
    }

    @GetMapping("/germplasm/{germplasmDbId}")
    public BrapiSingleResponse<GermplasmV2VO> byGermplasmDbId(@PathVariable String germplasmDbId) throws Exception {

        GermplasmV2VO gV2Vo = germplasmDao.getByGermplasmDbId(germplasmDbId);
        return BrapiSingleResponse.brapiResponseOf(gV2Vo, Pageable.ofSize(1));
        //return gV2Vo;
    }



    @GetMapping("/collection")
    public BrapiListResponse<CollPopVO> getCollections(){
        return collectionDao.getAllCollections();

    }
}
