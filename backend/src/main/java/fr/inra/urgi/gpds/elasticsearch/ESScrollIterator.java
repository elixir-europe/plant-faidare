package fr.inra.urgi.gpds.elasticsearch;

import fr.inra.urgi.gpds.elasticsearch.document.DocumentAnnotationUtil;
import fr.inra.urgi.gpds.elasticsearch.document.DocumentMetadata;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * Scroll through all result of an Elasticsearch query as an Iterator
 *
 * <b>!Warning!: This iterator is lazy. Pages will be fetched only when the iterator is consumed and uses
 * Elasticsearch scroll API which includes a timeout/keepAlive time after which you are not guaranteed access
 * to the remaining pages. Please make sure to consume the iterator within the
 * keep alive time {@link #DEFAULT_KEEP_ALIVE_TIME}</b>
 *
 * @author gcornut
 */
public class ESScrollIterator<T> implements Iterator<T> {

    public static final TimeValue DEFAULT_KEEP_ALIVE_TIME = new TimeValue(60, TimeUnit.SECONDS);

    private static final Logger LOGGER = LoggerFactory.getLogger(ESScrollIterator.class);

    private final Class<T> documentClass;
    private final RestHighLevelClient client;
    private final ESRequestFactory requestFactory;
    private final ESResponseParser parser;
    private final long totalHits;

    private long hitIndex;
    private String scrollId;
    private Iterator<T> currentIterator;

    public ESScrollIterator(
        RestHighLevelClient client,
        ESRequestFactory requestFactory,
        ESResponseParser parser, Class<T> documentClass,
        QueryBuilder query,
        int fetchSize
    ) {
        this.client = client;
        this.requestFactory = requestFactory;
        this.parser = parser;
        this.documentClass = documentClass;

        DocumentMetadata<T> documentMetadata = DocumentAnnotationUtil.getDocumentObjectMetadata(documentClass);

        SearchRequest request = requestFactory
            .prepareSearch(documentMetadata.getDocumentType(), query)
            .scroll(DEFAULT_KEEP_ALIVE_TIME);

        request.source()
            .size(fetchSize)
            .sort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC);

        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.scrollId = response.getScrollId();
        this.totalHits = response.getHits().getTotalHits();
        this.hitIndex = 0;
        this.currentIterator = parseIterator(response);
    }

    private Iterator<T> parseIterator(SearchResponse pageResponse) {
        try {
            List<T> hits = parser.parseHits(pageResponse, documentClass);
            if (hits != null) {
                return hits.iterator();
            }
            throw new RuntimeException("Could not parse Elasticsearch hits");
        } catch (IOException | ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasNext() {
        if (currentIterator != null && currentIterator.hasNext()) {
            return true;
        } else if (totalHits > hitIndex) {

            SearchScrollRequest request = requestFactory
                .getSearchScroll(scrollId)
                .scroll(DEFAULT_KEEP_ALIVE_TIME);

            LOGGER.debug("Scroll new page: " + scrollId);

            SearchResponse response = null;
            try {
                response = client.scroll(request, RequestOptions.DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.scrollId = response.getScrollId();
            this.currentIterator = parseIterator(response);
            return true;
        }
        return false;
    }

    @Override
    public T next() {
        if (hasNext()) {
            hitIndex++;
            return currentIterator.next();
        }
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
