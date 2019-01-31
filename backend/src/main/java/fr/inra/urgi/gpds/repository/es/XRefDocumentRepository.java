package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.domain.xref.XRefDocumentSearchCriteria;
import fr.inra.urgi.gpds.domain.xref.XRefDocumentVO;
import fr.inra.urgi.gpds.elasticsearch.repository.ESFindRepository;

public interface XRefDocumentRepository extends ESFindRepository<XRefDocumentSearchCriteria, XRefDocumentVO> {
    @Override
    PaginatedList<XRefDocumentVO> find(XRefDocumentSearchCriteria criteria);
}
