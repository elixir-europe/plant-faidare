package fr.inra.urgi.faidare.repository.es;

import fr.inra.urgi.faidare.domain.criteria.GermplasmAttributeCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmAttributeValueListVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.elasticsearch.ESRequestFactory;
import fr.inra.urgi.faidare.elasticsearch.ESResponseParser;
import fr.inra.urgi.faidare.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.faidare.elasticsearch.repository.impl.ESGenericFindRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author gcornut
 */
@Repository
public class GermplasmAttributeRepositoryImpl
    implements GermplasmAttributeRepository {

    private ESFindRepository<GermplasmAttributeCriteria, GermplasmAttributeValueListVO> findAttributeRepository;

    @Autowired
    public GermplasmAttributeRepositoryImpl(
        ESResponseParser parser,
        RestHighLevelClient client,
        ESRequestFactory requestFactory
    ) {
        Class<GermplasmAttributeValueListVO> voClass = GermplasmAttributeValueListVO.class;
        findAttributeRepository = new ESGenericFindRepository<>(client, requestFactory, voClass, parser);
    }

    @Override
    public PaginatedList<GermplasmAttributeValueListVO> find(GermplasmAttributeCriteria criteria) {
        return findAttributeRepository.find(criteria);
    }

}
