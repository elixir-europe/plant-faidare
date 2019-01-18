package fr.inra.urgi.gpds.repository.es;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiLocation;
import fr.inra.urgi.gpds.domain.criteria.StudyCriteria;
import fr.inra.urgi.gpds.domain.data.impl.LocationVO;
import fr.inra.urgi.gpds.domain.data.impl.StudyDetailVO;
import fr.inra.urgi.gpds.domain.data.impl.StudySummaryVO;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.elasticsearch.ESRequestFactory;
import fr.inra.urgi.gpds.elasticsearch.ESResponseParser;
import fr.inra.urgi.gpds.elasticsearch.document.DocumentAnnotationUtil;
import fr.inra.urgi.gpds.elasticsearch.document.DocumentMetadata;
import fr.inra.urgi.gpds.elasticsearch.query.impl.ESGenericQueryFactory;
import fr.inra.urgi.gpds.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.ESGetByIdRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.impl.ESGenericFindRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.impl.ESGenericGetByIdRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.filter;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

/**
 * @author gcornut
 *
 *
 */
@Repository
public class StudyRepositoryImpl
		implements StudyRepository {

    private final RestHighLevelClient client;
	private final ESRequestFactory requestFactory;

	private final ESGetByIdRepository<StudyDetailVO> getByIdRepository;
	private final ESFindRepository<StudyCriteria, StudySummaryVO> findRepository;
	private final DocumentMetadata<StudySummaryVO> studySummaryMetadata;
    private final ESResponseParser parser;
	private final LocationRepository locationRepository;

	private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
	public StudyRepositoryImpl(
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        ESResponseParser parser,
        LocationRepository locationRepository
    ) {
        this.client = client;
        this.requestFactory = requestFactory;
        this.parser = parser;
        this.locationRepository = locationRepository;

        this.getByIdRepository = new ESGenericGetByIdRepository<>(client, requestFactory, StudyDetailVO.class, this.parser);

		Class<StudySummaryVO> voClass = StudySummaryVO.class;
		this.studySummaryMetadata = DocumentAnnotationUtil.getDocumentObjectMetadata(voClass);

		ESGenericQueryFactory<StudyCriteria> queryFactory = new ESGenericQueryFactory<>();
		this.findRepository = new ESGenericFindRepository<>(this.client, requestFactory, voClass, queryFactory, this.parser);

	}

	@Override
	public StudyDetailVO getById(String id) {
		StudyDetailVO study = getByIdRepository.getById(id);
		if (study != null) {
			BrapiLocation location = study.getLocation();
			if (location != null) {
				// Replace location with complete detail
				LocationVO locationDetail = locationRepository.getById(location.getLocationDbId());
				study.setLocation(locationDetail);
			}
		}
		return study;
	}

	@Override
	public PaginatedList<StudySummaryVO> find(StudyCriteria criteria) {
		return findRepository.find(criteria);
	}

	@Override
	public Set<String> getVariableIds(String studyDbId) {
		String documentType = studySummaryMetadata.getDocumentType();

		String termAggName = "termAgg";
		TermsAggregationBuilder termAgg = terms(termAggName)
				.field("observationVariableDbIds")
				.size(ESRequestFactory.MAX_TERM_AGG_SIZE);

		String filterAggName = "filterAgg";
		FilterAggregationBuilder filterAgg = filter(filterAggName, termQuery(studySummaryMetadata.getIdField(), studyDbId))
				.subAggregation(termAgg);

        SearchRequest request = requestFactory
            .prepareSearch(documentType);
        request.source().size(0).aggregation(filterAgg);

		logger.debug(request.toString());

        SearchResponse searchResponse;
        try {
            searchResponse = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<String> aggregationPath = Arrays.asList(filterAggName, termAggName);
		List<String> ids = parser.parseTermAggKeys(searchResponse, aggregationPath);
		if (ids == null) {
			return null;
		}
		return new LinkedHashSet<>(ids);
	}

}
