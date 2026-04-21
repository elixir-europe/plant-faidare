package fr.inrae.urgi.faidare.utils;

import fr.inrae.urgi.faidare.dao.v2.GermplasmV2Criteria;
import fr.inrae.urgi.faidare.dao.v2.GermplasmV2CriteriaGet;

import java.util.List;

public class GermplasmV2Mapper {

    public static GermplasmV2Criteria convertGetToPost(GermplasmV2CriteriaGet get) {
        GermplasmV2Criteria post = new GermplasmV2Criteria();
        if(get.getAccessionNumber() != null) post.setAccessionNumbers(List.of(get.getAccessionNumber()));
        if(get.getBinomialName() != null) post.setBinomialNames(List.of(get.getBinomialName()));
        if(get.getCollection() != null) post.setCollections(List.of(get.getCollection()));
        if(get.getCommonCropName() != null) post.setCommonCropNames(List.of(get.getCommonCropName()));
        if(get.getExternalReferenceId() != null) post.setExternalReferenceIds(List.of(get.getExternalReferenceId()));
        if(get.getExternalReferenceSource() != null) post.setExternalReferenceSources(List.of(get.getExternalReferenceSource()));
        if(get.getFamilyCode() != null) post.setFamilyCodes(List.of(get.getFamilyCode()));
        if(get.getGermplasmDbId() != null) post.setGermplasmDbIds(List.of(get.getGermplasmDbId()));
        if(get.getGermplasmName() != null) post.setGermplasmNames(List.of(get.getGermplasmName()));
        if(get.getGermplasmPUI() != null) post.setGermplasmPUIs(List.of(get.getGermplasmPUI()));
        if(get.getInstituteCode() != null) post.setInstituteCodes(List.of(get.getInstituteCode()));
        if(get.getParentDbId() != null) post.setParentDbIds(List.of(get.getParentDbId()));
        if(get.getProgenyDbId() != null) post.setProgenyDbIds(List.of(get.getProgenyDbId()));
        if(get.getProgramDbId() != null) post.setProgramDbIds(List.of(get.getProgramDbId()));
        if(get.getProgramName() != null) post.setProgramNames(List.of(get.getProgramName()));
        if(get.getStudyDbId() != null) post.setStudyDbIds(List.of(get.getStudyDbId()));
        if(get.getStudyName() != null) post.setStudyNames(List.of(get.getStudyName()));
        if(get.getSynonym() != null) post.setSynonyms(List.of(get.getSynonym()));
        if(get.getTrialDbId() != null) post.setTrialDbIds(List.of(get.getTrialDbId()));
        if(get.getTrialName() != null) post.setTrialNames(List.of(get.getTrialName()));
        post.setPage(get.getPage());
        post.setPageSize(get.getPageSize());
        return post;
    }
}
