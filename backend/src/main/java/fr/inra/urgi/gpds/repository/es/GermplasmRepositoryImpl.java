package fr.inra.urgi.gpds.repository.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.inra.urgi.gpds.domain.criteria.GermplasmSearchCriteria;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.GermplasmVO;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.PedigreeVO;
import fr.inra.urgi.gpds.domain.data.impl.germplasm.ProgenyVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.elasticsearch.ESRequestFactory;
import fr.inra.urgi.gpds.elasticsearch.ESScrollIterator;
import fr.inra.urgi.gpds.elasticsearch.query.impl.ESGenericQueryFactory;
import fr.inra.urgi.gpds.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.ESGetByIdRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.impl.ESGenericFindRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.impl.ESGenericGetByIdRepository;
import fr.inra.urgi.gpds.utils.JacksonFactory;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Iterator;

@Repository
public class GermplasmRepositoryImpl implements GermplasmRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(GermplasmRepositoryImpl.class);

    private final RestHighLevelClient client;
	private final ESRequestFactory requestFactory;

	private final ESFindRepository<GermplasmSearchCriteria, GermplasmVO> findRepository;
	private final ESGetByIdRepository<GermplasmVO> getByIdRepository;
	private final ESGenericQueryFactory<Object> queryFactory;
	private final ObjectMapper mapper;

	@Autowired
	public GermplasmRepositoryImpl(RestHighLevelClient client, ESRequestFactory requestFactory) {
        this.client = client;
        this.requestFactory = requestFactory;
		this.mapper = JacksonFactory.createPermissiveMapper();
		Class<GermplasmVO> voClass = GermplasmVO.class;
		this.queryFactory = new ESGenericQueryFactory<>();
		this.findRepository = new ESGenericFindRepository<>(client, requestFactory, voClass);
		this.getByIdRepository = new ESGenericGetByIdRepository<>(client, requestFactory, voClass);
	}

	@Override
	public Iterator<GermplasmVO> scrollAll(GermplasmSearchCriteria criteria) {
		QueryBuilder query = queryFactory.createQuery(criteria);
		int fetchSize = criteria.getPageSize().intValue();
		return new ESScrollIterator<>(client, requestFactory, GermplasmVO.class, query, fetchSize);
	}

	@Override
	public GermplasmVO getById(String germplasmDbId) {
		return getByIdRepository.getById(germplasmDbId);
	}

	@Override
	public PaginatedList<GermplasmVO> find(GermplasmSearchCriteria criteria) {
		return findRepository.find(criteria);
	}

	@Override
	public PedigreeVO findPedigree(String germplasmDbId) {
		QueryBuilder termQuery = QueryBuilders.termQuery("germplasmDbId", germplasmDbId);
        SearchRequest request = requestFactory.prepareSearch("germplasmPedigree", termQuery);
        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PedigreeVO pedigreeVO = null;
		final SearchHits hits = response.getHits();

		LOGGER.debug("\n\nQuery from findPedigree :\n" + termQuery.toString() + "\n\n");

		if (hits.totalHits == 1) {
			// result found! \o/
			SearchHit hit = hits.getAt(0);
			String source = hit.getSourceAsString();
			try {
				pedigreeVO = mapper.readValue(source, PedigreeVO.class);
			} catch (IOException e) {
				LOGGER.error("Error occured when converting ES response to PedigreeVO: " + e.getMessage(), e);
			}
		} else if (hits.totalHits > 1){
			throw new IllegalStateException("Expected only 1 result for pedigree with germplasmDbId: " + germplasmDbId);
		}
		return pedigreeVO;
	}

	@Override
	public ProgenyVO findProgeny(String germplasmDbId) {
		QueryBuilder termQuery = QueryBuilders.termQuery("germplasmDbId", germplasmDbId);
        SearchRequest request = requestFactory.prepareSearch("germplasmProgeny", termQuery);
        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
		ProgenyVO progenyVO = null;
		final SearchHits hits = response.getHits();
		LOGGER.debug("\n\nQuery from findProgeny :\n" + termQuery.toString() + "\n\n");

		if (hits.totalHits == 1) {
			SearchHit hit = hits.getAt(0);
			String source = hit.getSourceAsString();
			try {
				progenyVO = mapper.readValue(source, ProgenyVO.class);
			} catch (IOException e) {
				LOGGER.error("Error occured when converting ES response to ProgenyVO: " + e.getMessage(), e);
			}
		} else if (hits.totalHits > 1){
			throw new IllegalStateException("Expected only 1 result for progeny with germplasmDbId: " + germplasmDbId);
		}
		return progenyVO;
	}
}
