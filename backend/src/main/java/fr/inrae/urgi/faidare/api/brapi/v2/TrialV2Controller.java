package fr.inrae.urgi.faidare.api.brapi.v2;

import fr.inrae.urgi.faidare.dao.v2.TrialCriteria;
import fr.inrae.urgi.faidare.dao.v2.TrialV2Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.TrialV2VO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Breeding API latest version (V2)", description = "BrAPI full specifications : https://brapi.org/specification")
@RestController
@RequestMapping({"/brapi/v2"})
public class TrialV2Controller {

    private final TrialV2Dao trialDao;

    public TrialV2Controller(TrialV2Dao trialDao) {
        this.trialDao = trialDao;
    }

    @GetMapping("/trial/{trialDbId}")
    public BrapiSingleResponse<TrialV2VO> byTrialDbId(@PathVariable String trialDbId) throws Exception {
        TrialV2VO tV2Vo = trialDao.getByTrialDbId(trialDbId);
        return BrapiSingleResponse.brapiResponseOf(tV2Vo, Pageable.ofSize(1));
    }

    @GetMapping("/trials")
    public  BrapiListResponse<TrialV2VO> trial(@ModelAttribute TrialCriteria tCrit){
        BrapiListResponse<TrialV2VO> trialV2VOs = trialDao.findTrialsByCriteria(tCrit);
        return trialV2VOs;
    }
    @PostMapping(value = "/search/trial", consumes = "application/json", produces = "application/json")
    public BrapiListResponse<TrialV2VO> searchTrial(@RequestBody TrialCriteria tCrit){
        return trialDao.findTrialsByCriteria(tCrit);
    }
}
