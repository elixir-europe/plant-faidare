package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.criteria.GermplasmAttributeCriteria;
import fr.inra.urgi.gpds.domain.data.germplasm.GermplasmAttributeValueListVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.elasticsearch.ESRequestFactory;
import fr.inra.urgi.gpds.elasticsearch.ESResponseParser;
import fr.inra.urgi.gpds.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.impl.ESGenericFindRepository;
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
