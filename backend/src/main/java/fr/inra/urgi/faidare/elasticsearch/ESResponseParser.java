package fr.inra.urgi.faidare.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.base.Joiner;
import fr.inra.urgi.faidare.domain.JSONView;
import fr.inra.urgi.faidare.domain.datadiscovery.data.FacetTermImpl;
import fr.inra.urgi.faidare.elasticsearch.document.DocumentAnnotationUtil;
import fr.inra.urgi.faidare.elasticsearch.document.DocumentMetadata;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.SingleBucketAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author gcornut
 */
@Component
public class ESResponseParser {

    private final ObjectMapper objectMapper;

    public ESResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Parse the total hits number from an ElasticSearch search response
     *
     * @param response the ElasticSearch search response
     */
    public Long parseTotalHits(SearchResponse response) {
        if (response == null) return null;
        SearchHits hits = response.getHits();
        if (hits == null) return null;
        return hits.getTotalHits().value;
    }

    /**
     * Parse hits as value objects from an ElasticSearch search response
     *
     * @param response the ElasticSearch search response
     * @param clazz    the value object class
     * @return the list of value object
     */
    public <T> List<T> parseHits(SearchResponse response, Class<T> clazz) throws IOException, ReflectiveOperationException {
        SearchHits hits = response.getHits();
        if (hits == null) return null;

        SearchHit[] searchHits = hits.getHits();
        if (searchHits == null) return null;

        DocumentMetadata<T> metadata = DocumentAnnotationUtil.getDocumentObjectMetadata(clazz);

        List<T> parsedHits = new ArrayList<>();

        // Base JSON Object reader configured with the internal view
        ObjectReader baseReader = objectMapper.readerWithView(JSONView.Internal.class);

        ObjectReader rootDocumentReader = baseReader.forType(clazz);
        for (SearchHit hit : searchHits) {
            T value = rootDocumentReader.readValue(hit.getSourceAsString());

            if (hit.getInnerHits() != null) {

                for (String fieldName : metadata.getFieldsByName().keySet()) {
                    DocumentMetadata.Field field = metadata.getFieldsByName().get(fieldName);
                    if (!field.isNestedObject()) {
                        continue;
                    }
                    String stringPath = Joiner.on(".").join(field.getPath());

                    ObjectReader nestedReader = baseReader.forType(field.getFieldClass());

                    SearchHits innerHits = hit.getInnerHits().get(stringPath);
                    if (innerHits == null) {
                        throw new IllegalArgumentException("Could not find inner hit for path: " + stringPath);
                    }

                    // Collect inner hits
                    List<Object> innerValues = new ArrayList<>();
                    for (SearchHit innerHit : innerHits.getHits()) {
                        innerValues.add(nestedReader.readValue(innerHit.getSourceAsString()));
                    }

                    // Set inner hit value
                    Method nestedSetter = field.getDescriptor().getWriteMethod();
                    nestedSetter.invoke(value, innerValues);
                }
            }

            parsedHits.add(value);
        }
        return parsedHits;
    }

    /**
     * Parse keys of a term aggregation result
     *
     * @param response        the ElasticSearch search response
     * @param aggregationPath array of aggregation name used as path to find the term aggregation
     */
    public List<String> parseTermAggKeys(SearchResponse response, List<String> aggregationPath) {
        Terms termAgg = findAggregation(response, aggregationPath, Terms.class);
        if (termAgg == null) return null;

        List<String> keys = new ArrayList<>();
        for (Terms.Bucket bucket : termAgg.getBuckets()) {
            keys.add(bucket.getKey().toString());
        }
        return keys;
    }

    /**
     * Find aggregation result in search response using the aggregation path and its type
     */
    private <T> T findAggregation(
        SearchResponse response, List<String> aggregationPath, Class<T> aggregationType
    ) {
        Aggregations aggregations = response.getAggregations();
        if (aggregations == null) return null;

        Aggregation aggregation = null;
        for (String aggregationName : aggregationPath) {
            if (aggregation == null || aggregation instanceof SingleBucketAggregation) {
                if (aggregation != null) {
                    aggregations = ((SingleBucketAggregation) aggregation).getAggregations();
                }

                aggregation = aggregations.get(aggregationName);
            } else {
                // Invalid aggregation path
                return null;
            }
        }
        try {
            return aggregationType.cast(aggregation);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Parse terms aggregation results into facet VO
     */
    public List<FacetTermImpl> parseFacetTerms(SearchResponse response, List<String> aggregationPath) {
        Terms termAgg = findAggregation(response, aggregationPath, Terms.class);
        if (termAgg == null) return null;

        List<FacetTermImpl> terms = new ArrayList<>();
        for (Terms.Bucket bucket : termAgg.getBuckets()) {
            terms.add(new FacetTermImpl(bucket.getKey().toString(), bucket.getDocCount()));
        }
        return terms;
    }

    /**
     * Parse terms aggregation results into facet VO
     */
    public List<FacetTermImpl> parseFacetTerms(SearchResponse response, String... aggregationPath) {
        return parseFacetTerms(response, Arrays.asList(aggregationPath));
    }
}
