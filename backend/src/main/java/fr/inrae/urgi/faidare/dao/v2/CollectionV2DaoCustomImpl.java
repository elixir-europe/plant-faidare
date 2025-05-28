package fr.inrae.urgi.faidare.dao.v2;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import fr.inrae.urgi.faidare.api.brapi.v2.BrapiListResponse;
import fr.inrae.urgi.faidare.domain.CollPopVO;
import fr.inrae.urgi.faidare.domain.brapi.v2.GermplasmV2VO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class CollectionV2DaoCustomImpl implements CollectionV2DaoCustom{
    @Autowired
    private ElasticsearchTemplate esTemplate;
    @Override
    /**
     * Mcok implementation, to be replaced with a real call to callPop index
     */
    public BrapiListResponse<CollPopVO> getAllCollections() {
        Query query= esTemplate.matchAllQuery();
        query.setTrackTotalHits(true);
        Query q = NativeQuery.builder()
                .withAggregation("collection_name", Aggregation.of(a -> a.terms(ta -> ta.field("collection.name").size(20000))))
                //.withQuery(QueryBuilders.matchAllQueryAsQuery())
                .withMaxResults(0)
                .build();
        q.setTrackTotalHits(true);
        SearchHits<GermplasmV2VO> sHits = esTemplate.search(q, GermplasmV2VO.class);
        List<ElasticsearchAggregation> collPopAggregations = (List<ElasticsearchAggregation>) sHits.getAggregations().aggregations();

        List<StringTermsBucket> b = collPopAggregations.get(0).aggregation().getAggregate().sterms().buckets().array();

        BrapiListResponse<CollPopVO> response = new BrapiListResponse<CollPopVO>();
        List<CollPopVO> resultList = new ArrayList<CollPopVO>();
        b.forEach(collName -> resultList.add(new CollPopVO(collName.key().stringValue(), collName.key().stringValue())));
        response.getResult().setData(resultList);
        response.getMetadata().getPagination().setTotalCount((resultList.size()));
        return response;
    }
}
