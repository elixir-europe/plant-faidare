package fr.inrae.urgi.faidare.api.brapi.v1;


import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.api.brapi.v2.BrapiSingleResponse;
import fr.inrae.urgi.faidare.dao.v1.GermplasmV1Dao;
import fr.inrae.urgi.faidare.dao.v2.CollectionV2Dao;
import fr.inrae.urgi.faidare.domain.CollPopVO;
import fr.inrae.urgi.faidare.domain.brapi.v1.GermplasmV1VO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@Tag(name = "Breeding API", description = "BrAPI endpoint")
@RestController
@RequestMapping({"/brapi/v1"})
public class GermplasmV1Controller {
    private final GermplasmV1Dao germplasmDao;
    private final CollectionV2Dao collectionDao;


    public GermplasmV1Controller(GermplasmV1Dao germplasmDao, CollectionV2Dao collectionDao) {
        this.germplasmDao = germplasmDao;
        this.collectionDao = collectionDao;
    }




    @Value("classpath:calls.json")
    Resource serverInfoFile;

    @GetMapping("/calls")
    public @ResponseBody JsonNode calls() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(serverInfoFile.getInputStream());
    }

    @GetMapping("/germplasm/{germplasmDbId}")
    public BrapiSingleResponse<GermplasmV1VO> byGermplasmDbId(@PathVariable String germplasmDbId) throws Exception {

        GermplasmV1VO gVo = germplasmDao.getByGermplasmDbId(germplasmDbId);
        return BrapiSingleResponse.brapiResponseOf(gVo, Pageable.ofSize(1));
    }

    @GetMapping("/germplasm")
    public BrapiListResponse<GermplasmV1VO> allGermplasms(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                          @RequestParam(required = false, defaultValue = "10")  Integer pageSize) throws Exception {


        Page<GermplasmV1VO> gVos = germplasmDao.findAll(Pageable.ofSize(pageSize).withPage(page));
        return BrapiListResponse.brapiResponseForPageOf(gVos);
    }

    //GemplasmMCPD is assumed to be broken/not loaded and not used in curent FAIDARE cards

    @GetMapping("/collection")
    public BrapiListResponse<CollPopVO> getAllCollections(){
        return collectionDao.getAllCollections();
    }


}
