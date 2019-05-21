package fr.inra.urgi.faidare.elasticsearch.query.impl;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import fr.inra.urgi.faidare.elasticsearch.criteria.AnnotatedCriteriaMapper;
import fr.inra.urgi.faidare.elasticsearch.criteria.annotation.DocumentPath;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMapping;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMappingCriterion;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMappingTree;
import fr.inra.urgi.faidare.elasticsearch.criteria.mapping.CriteriaMappingTreeNode;
import fr.inra.urgi.faidare.elasticsearch.document.DocumentMetadata;
import fr.inra.urgi.faidare.elasticsearch.query.ESQueryFactory;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.*;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Generic Elasticsearch query generator for criteria mapped on value object via
 * {@link DocumentPath} annotations
 *
 * @author gcornut
 */
public class ESGenericQueryFactory<C> implements ESQueryFactory<C> {

    private static final Integer MAX_INNER_HIT_SIZE = 10000;

    /**
     * Generate an ElasticSearch query from a criteria with fields that have been
     * annotated with {@link DocumentPath}
     *
     * @param criteria the criteria from which to generate the query
     * @return an ES QueryBuilder
     * @throws ESQueryGenerationException when a reflective introspection failed on the criteria or the
     *                                    mapped value object
     */
    @Override
    public QueryBuilder createQuery(C criteria) {
        try {
            CriteriaMapping voMappingToCriteria = AnnotatedCriteriaMapper.getMapping(criteria.getClass());
            DocumentMetadata<?> documentMetadata = voMappingToCriteria.getDocumentMetadata();

            List<QueryBuilder> queries = createQueryFromMapping(criteria, null, null, voMappingToCriteria, documentMetadata);

            if (!queries.isEmpty()) {
                return andQueries(queries);
            } else {
                return matchAllQuery();
            }
        } catch (Exception e) {
            throw new ESQueryGenerationException(e);
        }
    }

    /**
     * Same as {@link ESGenericQueryFactory#createQuery(Object)} but with a list of document fields to exclude from query
     */
    public QueryBuilder createQueryExcludingFields(C criteria, String... excludeDocumentFields) {
        try {
            CriteriaMapping voMappingToCriteria = AnnotatedCriteriaMapper.getMapping(criteria.getClass());
            DocumentMetadata<?> documentMetadata = voMappingToCriteria.getDocumentMetadata();
            Set<String> excludedDocumentFields = ImmutableSet.copyOf(excludeDocumentFields);

            List<QueryBuilder> queries = createQueryFromMapping(criteria, null, excludedDocumentFields, voMappingToCriteria, documentMetadata);

            if (!queries.isEmpty()) {
                return andQueries(queries);
            } else {
                return matchAllQuery();
            }
        } catch (Exception e) {
            throw new ESQueryGenerationException(e);
        }
    }

    /**
     * Same as {@link ESGenericQueryFactory#createQuery(Object)} but with a list of document fields to include from query (excluding all others)
     */
    public QueryBuilder createQueryIncludingFields(C criteria, String... includeDocumentFields) {
        try {
            CriteriaMapping voMappingToCriteria = AnnotatedCriteriaMapper.getMapping(criteria.getClass());
            DocumentMetadata<?> documentMetadata = voMappingToCriteria.getDocumentMetadata();
            Set<String> includedDocumentFields = ImmutableSet.copyOf(includeDocumentFields);

            List<QueryBuilder> queries = createQueryFromMapping(criteria, includedDocumentFields, null, voMappingToCriteria, documentMetadata);

            if (!queries.isEmpty()) {
                return andQueries(queries);
            } else {
                return matchAllQuery();
            }
        } catch (Exception e) {
            throw new ESQueryGenerationException(e);
        }
    }

    /**
     * Generate a query from a criteria
     */
    private List<QueryBuilder> createQueryFromMapping(
        C criteria,
        Set<String> includedDocumentFields,
        Set<String> excludedDocumentFields,
        CriteriaMappingTreeNode criteriaMapping,
        DocumentMetadata<?> documentMetadata
    ) throws Exception {
        List<QueryBuilder> queries = new ArrayList<>();
        Class criteriaClass = criteria.getClass();

        for (CriteriaMappingTree treeNode : criteriaMapping.getMappingTree().values()) {
            List<String> documentFieldPath = treeNode.getDocumentFieldPath();
            String stringPath = Joiner.on(".").join(documentFieldPath);

            if (includedDocumentFields != null) {
                boolean someStartWithPath = false;
                for (String includedDocumentField : includedDocumentFields) {
                    if (includedDocumentField.startsWith(stringPath)) {
                        someStartWithPath = true;
                    }
                }
                if (!someStartWithPath) {
                    continue;
                }
            }
            if (excludedDocumentFields != null && excludedDocumentFields.contains(stringPath)) {
                continue;
            }

            // Metadata on the current document field
            DocumentMetadata.Field documentField = documentMetadata.getByPath(documentFieldPath);

            if (treeNode instanceof CriteriaMappingCriterion) {
                // Current node is a leaf (an actual criterion from the criteria object)
                CriteriaMappingCriterion criterion = (CriteriaMappingCriterion) treeNode;

                if (!criterion.isVirtualField() && documentField == null) {
                    // Document field should exist in document class (except if the field is virtual)
                    throw ESQueryGenerationException.couldNotFindDocumentField(
                        stringPath,
                        documentMetadata.getDocumentClass()
                    );
                }

                // Criterion value
                Object criterionValue = criterion.getValue(criteria);

                // Ignore empty criteria
                if (isBlank(criterionValue)) {
                    continue;
                }

                List<QueryBuilder> subQueries = createQueriesFromField(stringPath, criteriaClass, criterion, criterionValue);
                if (!subQueries.isEmpty()) {
                    queries.addAll(subQueries);
                }
            }

            if (treeNode instanceof CriteriaMappingTreeNode) {
                CriteriaMappingTreeNode nodeWithChildren = (CriteriaMappingTreeNode) treeNode;

                // Generate queries for criteria mapped in the
                List<QueryBuilder> subQueries = createQueryFromMapping(
                    criteria, includedDocumentFields, excludedDocumentFields, nodeWithChildren,
                    documentMetadata);

                if (!subQueries.isEmpty()) {
                    if (documentField.isNestedObject()) {
                        // Add nested query
                        InnerHitBuilder inner = new InnerHitBuilder().setSize(MAX_INNER_HIT_SIZE);
                        NestedQueryBuilder nested = nestedQuery(stringPath, andQueries(subQueries), ScoreMode.None).innerHit(inner);
                        queries.add(nested);
                    } else {
                        // Add nested query
                        queries.addAll(subQueries);
                    }
                }
            }
        }
        return queries;
    }

    private boolean isBlank(Object object) {
        return object == null || (object instanceof Collection && ((Collection) object).isEmpty());
    }

    /**
     * Generate a query for a criteria field
     */
    private List<QueryBuilder> createQueriesFromField(
        String documentField, Class criteriaClass, CriteriaMappingCriterion criterion, Object value
    ) throws ESQueryGenerationException {
        Class<? extends QueryBuilder> queryType = criterion.getQueryType();
        String criterionName = criterion.getName();

        // Range query
        if (queryType == RangeQueryBuilder.class) {
            if (!(value instanceof List) || ((List) value).size() > 2) {
                throw ESQueryGenerationException.invalidRange(criterionName);
            }
            List valueList = (List) value;
            RangeQueryBuilder range = rangeQuery(documentField)
                .from(valueList.get(0))
                .to(valueList.get(1));
            return Collections.singletonList(range);
        }

        // Term query
        if (queryType == TermQueryBuilder.class || queryType == TermsQueryBuilder.class) {
            if (value instanceof Collection) {
                TermsQueryBuilder terms = termsQuery(documentField, (Collection) value);
                return Collections.singletonList(terms);
            }

            if (value instanceof String || value instanceof Number || value instanceof Boolean) {
                TermQueryBuilder term = termQuery(documentField, value);
                return Collections.singletonList(term);
            }
        }

        // Match phrase query
        if (queryType == MatchPhraseQueryBuilder.class) {
            if (value instanceof Collection) {
                List<QueryBuilder> matches = new ArrayList<>();
                for (Object v : ((Collection) value)) {
                    matches.add(matchPhraseQuery(documentField, v));
                }
                return matches;
            }
            if (value instanceof String) {
                MatchPhraseQueryBuilder match = matchPhraseQuery(documentField, value);
                return Collections.singletonList(match);
            }
        }

        throw ESQueryGenerationException.notImplemented(criterionName, criteriaClass, queryType, value);
    }

    public static class ESQueryGenerationException extends RuntimeException {
        ESQueryGenerationException(String message) {
            super(message);
        }

        ESQueryGenerationException(Throwable e) {
            super(e);
        }

        ESQueryGenerationException(String message, Throwable e) {
            super(message, e);
        }

        static ESQueryGenerationException couldNotGenerateQueryForField(
            String criterionName, Class criteriaClass, Throwable e
        ) {
            return new ESQueryGenerationException(
                "Could not generate ES query for criterion '" + criterionName + "' " +
                    "in criteria '" + criteriaClass.getSimpleName() + "'", e
            );
        }

        static ESQueryGenerationException invalidRange(String criteriaName) {
            return new ESQueryGenerationException(
                "Range criteria " + criteriaName + " should be a list of 2 elements (lower bound and upper bound)");
        }

        static ESQueryGenerationException notImplemented(
            String criterionName, Class criteriaClass, Class queryBuilder, Object value
        ) {
            Throwable notImplementedException = new NotImplementedException(
                "No query generation implemented for query type '" + queryBuilder.getSimpleName() + "' " +
                    "and criteria value '" + value + "'"
            );
            return ESQueryGenerationException.couldNotGenerateQueryForField(
                criterionName, criteriaClass, notImplementedException
            );
        }

        static ESQueryGenerationException couldNotFindDocumentField(String subPath, Class<?> documentClass) {
            return new ESQueryGenerationException(
                "Could not find field '" + subPath + "' in document class '" + documentClass + "'"
            );
        }
    }

    /**
     * Combine queries into a bool must query
     *
     * @return null if not queries given; the first query if only only query is given; a bool query otherwise
     */
    public static QueryBuilder andQueries(List<QueryBuilder> queries) {
        if (queries == null || queries.isEmpty()) {
            return null;
        } else if (queries.size() == 1) {
            return queries.get(0);
        } else {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (QueryBuilder query : queries) {
                boolQueryBuilder.must(query);
            }
            return boolQueryBuilder;
        }
    }

    public static QueryBuilder andQueries(QueryBuilder... queries) {
        return andQueries(Arrays.asList(queries));
    }
}
