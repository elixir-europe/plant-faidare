package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentSearchCriteria;
import fr.inra.urgi.faidare.domain.xref.XRefDocumentVO;
import fr.inra.urgi.faidare.elasticsearch.repository.ESFindRepository;

public interface XRefDocumentRepository extends ESFindRepository<XRefDocumentSearchCriteria, XRefDocumentVO> {
    @Override
    PaginatedList<XRefDocumentVO> find(XRefDocumentSearchCriteria criteria);
}
