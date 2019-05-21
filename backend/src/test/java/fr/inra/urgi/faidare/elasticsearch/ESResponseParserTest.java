package fr.inra.urgi.faidare.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.inra.urgi.faidare.elasticsearch.fixture.DocumentObject;
import org.assertj.core.util.Lists;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.beans.IntrospectionException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@Import({ESResponseParser.class, ObjectMapper.class})
class ESResponseParserTest {

    @Autowired
    ESResponseParser parser;

    @Autowired
    ObjectMapper mapper;

    @Test
    void should_Parse_Total_Hits() {
        Long expectedTotalHits = 11L;
        SearchHit[] hitsArray = {};
        float maxScore = 100;

        // Can't mock SearchHits since it is a final class
        SearchHits hits = new SearchHits(hitsArray, expectedTotalHits, maxScore);

        SearchResponse response = mock(SearchResponse.class);
        when(response.getHits()).thenReturn(hits);

        Long actualTotalHits = parser.parseTotalHits(response);

        assertThat(actualTotalHits).isEqualTo(expectedTotalHits);
    }

    @Test
    void should_Fail_Parse_Total_Hits() {
        Long expectedTotalHits = null;

        SearchResponse response = mock(SearchResponse.class);
        when(response.getHits()).thenReturn(null);

        Long actualTotalHits = parser.parseTotalHits(response);

        assertThat(actualTotalHits).isEqualTo(expectedTotalHits);
    }

    @Test
    void should_Parse_Hits() throws IOException, ReflectiveOperationException {
        DocumentObject object1 = new DocumentObject();
        object1.id = 1L;
        object1.name = "object1";
        DocumentObject object2 = new DocumentObject();
        object2.id = 2L;
        object2.name = "object2";
        DocumentObject object3 = new DocumentObject();
        object3.id = 3L;
        object3.name = "object3";
        List<DocumentObject.Nested> nestedList = new ArrayList<>();
        DocumentObject.Nested nested1 = new DocumentObject.Nested();
        nested1.id = "N1";
        nestedList.add(nested1);
        DocumentObject.Nested nested2 = new DocumentObject.Nested();
        nested2.id = "N2";
        nestedList.add(nested2);
        object3.nested = nestedList;
        List<DocumentObject> expectedDocumentObjects = Arrays.asList(object1, object2, object3);

        SearchHit hit1 = mockSearchHit(object1);
        SearchHit hit2 = mockSearchHit(object2);
        SearchHit hit3 = mockSearchHit(object3);

        // Can't mock SearchHits since it is a final class
        SearchHits hits = new SearchHits(new SearchHit[]{hit1, hit2, hit3}, 3, 100);

        SearchResponse response = mock(SearchResponse.class);
        when(response.getHits()).thenReturn(hits);

        List<DocumentObject> actualDocumentObject = parser.parseHits(response, DocumentObject.class);

        assertThat(actualDocumentObject)
            .isNotNull().isNotEmpty()
            .containsOnlyElementsOf(expectedDocumentObjects);
    }

    private SearchHit mockSearchHit(DocumentObject object) throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, object);
        XContentBuilder content = XContentFactory.jsonBuilder(out);
        BytesReference bytes = BytesReference.bytes(content);

        // Can't mock SearchHit since it is a final class
        SearchHit hit = new SearchHit(object.id.intValue());
        hit.sourceRef(bytes);
        return hit;
    }

    @Test
    void should_Fail_Parse_Hits() throws IOException, IntrospectionException, ReflectiveOperationException {
        // Return null if no hits
        SearchResponse response = mock(SearchResponse.class);
        when(response.getHits()).thenReturn(null);

        List<DocumentObject> result = parser.parseHits(response, DocumentObject.class);
        assertThat(result).isNull();

        // Return null if no hits in hits
        SearchHits hits = new SearchHits(null, 0, 100);
        when(response.getHits()).thenReturn(hits);

        List<DocumentObject> result2 = parser.parseHits(response, DocumentObject.class);
        assertThat(result2).isNull();
    }

    @Test
    void should_Parse_TermKeysAgg() {
        String termAggName = "termAgg";
        String filterAggName = "filterAgg";
        List<String> aggregationPath = Arrays.asList(filterAggName, termAggName);
        List<String> expectedKeys = Arrays.asList("key1", "key2", "key3");
        List<Long> expectedCounts = Arrays.asList(300L, 200L, 100L);
        SearchResponse response = mockFilterTermAggResponse(filterAggName, termAggName, expectedKeys, expectedCounts);

        List<String> actualKeys = parser.parseTermAggKeys(response, aggregationPath);

        assertThat(actualKeys).isNotNull().isNotEmpty().containsExactlyElementsOf(expectedKeys);
    }


    private SearchResponse mockFilterTermAggResponse(
        String filterAggName, String termAggName, List<String> keys, List<Long> counts
    ) {
        List<Terms.Bucket> buckets = new ArrayList<>();
        Iterator<String> keyIt = keys.iterator();
        Iterator<Long> countIt = counts.iterator();
        while (keyIt.hasNext() && countIt.hasNext()) {
            String key = keyIt.next();
            Long docCount = countIt.next();

            Terms.Bucket bucket = mock(Terms.Bucket.class);
            when(bucket.getKey()).thenReturn(key);
            when(bucket.getKeyAsString()).thenReturn(key);
            when(bucket.getDocCount()).thenReturn(docCount);
            buckets.add(bucket);
        }

        Terms termAgg = mock(Terms.class);
        doReturn(buckets).when(termAgg).getBuckets();
        doReturn(termAggName).when(termAgg).getName();

        Aggregations aggs2 = new Aggregations(Lists.newArrayList(termAgg));

        Filter filterAgg = mock(Filter.class);
        when(filterAgg.getAggregations()).thenReturn(aggs2);
        when(filterAgg.getName()).thenReturn(filterAggName);

        Aggregations aggs1 = new Aggregations(Lists.newArrayList(filterAgg));

        SearchResponse response = mock(SearchResponse.class);
        when(response.getAggregations()).thenReturn(aggs1);

        return response;
    }

}
