package fr.inrae.urgi.faidare.dao;

import fr.inrae.urgi.faidare.domain.XRefDocumentVO;

import java.util.List;


public interface XRefDocumentDao extends DocumentDao<XRefDocumentVO> {

    List<XRefDocumentVO> findByLinkedResourcesID(String linkedResourcesID);

}
