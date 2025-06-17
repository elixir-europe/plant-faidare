package fr.inrae.urgi.faidare.api.brapi.v2;

import fr.inrae.urgi.faidare.dao.v2.StudyCriteria;
import fr.inrae.urgi.faidare.dao.v2.StudyV2Dao;
import fr.inrae.urgi.faidare.domain.brapi.v2.StudyV2VO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Breeding API latest version (V2)", description = "BrAPI full specifications : https://brapi.org/specification")
@RestController
@RequestMapping({"/brapi/v2"})
public class StudyV2Controller {

    private final StudyV2Dao studyDao;

    public StudyV2Controller(StudyV2Dao studyDao) {
        this.studyDao = studyDao;
    }

    @GetMapping("study")
    public BrapiListResponse<StudyV2VO> study(
        @ModelAttribute StudyCriteria sCrit){
        BrapiListResponse<StudyV2VO> studyV2VOs = studyDao.findStudiesByCriteria(sCrit);
        return studyV2VOs;
    }

    @PostMapping(value = "/search/study", consumes = "application/json", produces = "application/json")
    public BrapiListResponse<StudyV2VO> searchStudy(@RequestBody StudyCriteria sCrit){

        return studyDao.findStudiesByCriteria(sCrit);
    }

    @GetMapping("/study/{studyDbId}")
    public BrapiSingleResponse<StudyV2VO> byStudyDbId(@PathVariable String studyDbId) throws Exception {

        StudyV2VO sV2Vo = studyDao.getByStudyDbId(studyDbId);
        return BrapiSingleResponse.brapiResponseOf(sV2Vo, Pageable.ofSize(1));
    }

    @GetMapping("/studies/{trialDbId}")
    public  BrapiListResponse<StudyV2VO> studiesByTrialDbId(@PathVariable List <String> trialDbId) throws Exception {

        StudyCriteria sCrit = new StudyCriteria();
        sCrit.setTrialDbIds(trialDbId);
        BrapiListResponse<StudyV2VO> studyV2VOs = studyDao.findStudiesByCriteria(sCrit);
        return  studyV2VOs;
    }




}
