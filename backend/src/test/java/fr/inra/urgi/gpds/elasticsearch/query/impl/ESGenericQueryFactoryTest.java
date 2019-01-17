package fr.inra.urgi.gpds.elasticsearch.query.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import fr.inra.urgi.gpds.domain.criteria.*;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.DocumentPath;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.QueryType;
import fr.inra.urgi.gpds.elasticsearch.criteria.fixture.ComplexAnnotatedCriteria;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.ScriptQueryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

/**
 * @author gcornut
 */
public class ESGenericQueryFactoryTest {

	@Document(type = "valueobject")
	private class ValueObject {
		String field1;
	}

	@CriteriaForDocument(ValueObject.class)
	public class UnimplementedQueryTypeCriteria {
		@DocumentPath("field1")
		@QueryType(ScriptQueryBuilder.class)
		String criteria1;
	}

	@Test
    void should_Fail_Generating_Not_Implemented_Query_Type() {
        ESGenericQueryFactory<UnimplementedQueryTypeCriteria> queryFactory = new ESGenericQueryFactory<>();
        UnimplementedQueryTypeCriteria criteria = new UnimplementedQueryTypeCriteria();
	    Assertions.assertThrows(
            ESGenericQueryFactory.ESQueryGenerationException.class,
            () -> queryFactory.createQuery(criteria)
        );
	}

	@CriteriaForDocument(ValueObject.class)
	public class RangeCriteria {
		@DocumentPath("field1")
		@QueryType(RangeQueryBuilder.class)
		List<String> rangeCriteria;

		public List<String> getRangeCriteria() {
			return rangeCriteria;
		}

		public void setRangeCriteria(List<String> rangeCriteria) {
			this.rangeCriteria = rangeCriteria;
		}
	}

	@Test
    void should_Fail_Generating_Invalid_Range() {
		ESGenericQueryFactory<RangeCriteria> queryFactory = new ESGenericQueryFactory<>();
		RangeCriteria criteria = new RangeCriteria();
		criteria.setRangeCriteria(newArrayList("a", "b", "c"));
        Assertions.assertThrows(
            ESGenericQueryFactory.ESQueryGenerationException.class,
            () -> queryFactory.createQuery(criteria)
        );
	}

	/**
	 * Simple term query without bool query
	 */
	@Test
    void should_Generate_Term_Query() {
		ProgramCriteria criteria = new ProgramCriteria();
		String id = "A";
		criteria.setProgramDbId(id);

		ESGenericQueryFactory<ProgramCriteria> queryFactory = new ESGenericQueryFactory<>();
		QueryBuilder query = queryFactory.createQuery(criteria);

		String expectedQuery = "{\n" +
				"  \"term\" : {\n" +
				"    \"programDbId\" : {\n" +
				"      \"value\" : \"" + id +  "\",\n" +
				"      \"boost\" : 1.0\n" +
				"    }\n" +
				"  }\n" +
				"}";
		String actualQuery = query.toString();
		assertThat(actualQuery).isEqualToIgnoringWhitespace(expectedQuery);
	}

	/**
	 * Match query
	 */
	@Test
    void should_Generate_Match_Query() throws IOException {
		DataDiscoveryCriteriaImpl criteria = new DataDiscoveryCriteriaImpl();
		String accessionSearchText = "charg";
		criteria.setAccessions(newArrayList(accessionSearchText));

		ESGenericQueryFactory<DataDiscoveryCriteriaImpl> queryFactory = new ESGenericQueryFactory<>();
		QueryBuilder query = queryFactory.createQuery(criteria);

		String expectedQuery = "{\n" +
				"  \"match_phrase\" : {\n" +
				"    \"germplasm.accession.suggest\" : {\n" +
				"      \"query\" : \"" + accessionSearchText + "\",\n" +
				"      \"slop\" : 0,\n" +
				"      \"zero_terms_query\" : \"NONE\",\n" +
				"      \"boost\" : 1.0\n" +
				"    }\n" +
				"  }\n" +
				"}";

		String actualQuery = query.toString();
		assertJsonEquals(actualQuery, expectedQuery);
	}

	/**
	 * Bool query made of terms queries
	 */
	@Test
    void should_Generate_Location_Query() throws IOException {
		LocationCriteria criteria = new LocationCriteria();
		criteria.setLocationTypes(Sets.newHashSet("C", "D"));

		ESGenericQueryFactory<LocationCriteria> queryFactory = new ESGenericQueryFactory<>();
		QueryBuilder query = queryFactory.createQuery(criteria);

		String expectedQuery = "{\n" +
				"  \"terms\" : {\n" +
				"    \"locationType\" : [\n" +
				"      \"D\",\n" +
				"      \"C\"\n" +
				"    ],\n" +
				"    \"boost\" : 1.0\n" +
				"  }\n" +
				"}";

		String actualQuery = query.toString();
		assertJsonEquals(actualQuery, expectedQuery);
	}

	/**
	 * Bool query made of term queries
	 */
	@Test
    void should_Generate_Program_Query() throws IOException {
		ProgramCriteria criteria = new ProgramCriteria();
		criteria.setProgramDbId("A");
		criteria.setName("B");

		ESGenericQueryFactory<ProgramCriteria> queryFactory = new ESGenericQueryFactory<>();
		QueryBuilder query = queryFactory.createQuery(criteria);

		String expectedQuery = readResource("expected/query1.json");
		String actualQuery = query.toString();
		assertJsonEquals(actualQuery, expectedQuery);
	}

	@Test
    void should_Generate_Simple_Bool_Terms_Query() throws Exception {
		StudySearchCriteria criteria = new StudySearchCriteria();
		criteria.setProgramNames(Sets.newHashSet("A", "B"));
		criteria.setStudyLocations(Sets.newHashSet("C", "D"));

		ESGenericQueryFactory<StudySearchCriteria> queryFactory = new ESGenericQueryFactory<>();
		QueryBuilder query = queryFactory.createQuery(criteria);

		String expectedQuery = readResource("expected/query5.json");
		String actualQuery = query.toString();
		assertJsonEquals(actualQuery, expectedQuery);
	}

	@Test
    void should_Generate_Complex_Query_With_Nested_Bool_Must_Terms_And_Range_Query() throws Exception {
		ObservationUnitCriteria criteria = new ObservationUnitCriteria();
		criteria.setObservationLevel("level1");
		criteria.setObservationVariableDbIds(Sets.newHashSet("VAR1", "VAR2"));
		criteria.setSeasonDbIds(Sets.newHashSet("SEASON1", "SEASON2"));
		criteria.setObservationTimeStampRange(newArrayList("TIMESTAMP1", "TIMESTAMP2"));

		ESGenericQueryFactory<ObservationUnitCriteria> queryFactory = new ESGenericQueryFactory<>();
		QueryBuilder query = queryFactory.createQuery(criteria);

		String expectedQuery = readResource("expected/query4.json");
		String actualQuery = query.toString();
		assertJsonEquals(actualQuery, expectedQuery);
	}

	@Test
    void should_Generate_Data_Discovery_Criteria() throws IOException {
		DataDiscoveryCriteriaImpl criteria = new DataDiscoveryCriteriaImpl();

		criteria.setAccessions(newArrayList("A1", "A2"));
		criteria.setCrops(newArrayList("C1"));
		criteria.setGermplasmLists(newArrayList("G1", "G2", "G3"));
		criteria.setObservationVariableIds(newArrayList("V1"));
		criteria.setSources(newArrayList("S1"));
		criteria.setTypes(newArrayList("T1", "T2"));

		ESGenericQueryFactory<DataDiscoveryCriteria> queryFactory =
				new ESGenericQueryFactory<>();


		QueryBuilder query = queryFactory.createQuery(criteria);
		String actualQuery = query.toString();

		String expectedQuery = readResource("expected/query2.json");
		assertJsonEquals(actualQuery, expectedQuery);
	}

	@Test
    void should_Generate_Criteria() throws IOException {
		ComplexAnnotatedCriteria criteria = new ComplexAnnotatedCriteria();

		criteria.setCriteria1(Arrays.asList("1", "2"));
		criteria.setCriteria2("3");
		criteria.setCriteria3("4");
		criteria.setCriteria4("5");
		criteria.setCriteria5("6");

		ESGenericQueryFactory<ComplexAnnotatedCriteria> queryFactory =
				new ESGenericQueryFactory<>();

		QueryBuilder query = queryFactory.createQuery(criteria);
		String actualQuery = query.toString();

		String expectedQuery = readResource("expected/query7.json");
		assertJsonEquals(actualQuery, expectedQuery);
	}

	@Test
    void should_Generate_Criteria_With_Exclusion() throws IOException {
		ComplexAnnotatedCriteria criteria = new ComplexAnnotatedCriteria();

		criteria.setCriteria1(Arrays.asList("1", "2"));
		criteria.setCriteria2("3");
		criteria.setCriteria3("4");
		criteria.setCriteria4("5");
		criteria.setCriteria5("6");

		ESGenericQueryFactory<ComplexAnnotatedCriteria> queryFactory =
				new ESGenericQueryFactory<>();

		QueryBuilder query = queryFactory.createQueryExcludingFields(
				criteria, "field2.schema:name", "field1"
		);
		String actualQuery = query.toString();

		String expectedQuery = readResource("expected/query3.json");
		assertJsonEquals(actualQuery, expectedQuery);
	}

	@Test
    void should_Generate_Criteria_With_Inclusion() throws IOException {
		ComplexAnnotatedCriteria criteria = new ComplexAnnotatedCriteria();

		criteria.setCriteria1(Arrays.asList("1", "2"));
		criteria.setCriteria2("3");
		criteria.setCriteria3("4");
		criteria.setCriteria4("5");
		criteria.setCriteria5("6");

		ESGenericQueryFactory<ComplexAnnotatedCriteria> queryFactory =
				new ESGenericQueryFactory<>();

		QueryBuilder query = queryFactory.createQueryIncludingFields(
				criteria, "field2.schema:name", "field1"
		);
		String actualQuery = query.toString();

		String expectedQuery = readResource("expected/query6.json");
		assertJsonEquals(actualQuery, expectedQuery);
	}

	public static void assertJsonEquals(String json1, String json2) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		JsonNode jsonNode1 = mapper.readTree(json1);
		JsonNode jsonNode2 = mapper.readTree(json2);

		assertJsonEquals(jsonNode1, jsonNode2);
	}

	private static void assertJsonEquals(JsonNode node1, JsonNode node2) {
		assertThat(node1.getNodeType()).isEqualTo(node2.getNodeType());

		if (node1.isObject()) {
			Set<String> fields1 = Sets.newHashSet(node1.fieldNames());
			Set<String> fields2 = Sets.newHashSet(node2.fieldNames());

			assertThat(fields1)
					.as("Actual json object node should have expected fields")
					.isEqualTo(fields2);

			for (String field : fields1) {
				assertJsonEquals(node1.get(field), node2.get(field));
			}

		} else if (node1.isArray()) {
			Set<JsonNode> elements1 = Sets.newHashSet(node1.elements());
			Set<JsonNode> elements2 = Sets.newHashSet(node1.elements());

			assertThat(elements1)
					.as("Actual json array node should contain exactly the expected elements")
					.isEqualTo(elements2);
		} else {
			assertThat(node1).isEqualTo(node2);
		}
	}

	/**
	 * Read package resource in a String
	 */
	private String readResource(String path) {
		try {
			return CharStreams.toString(new InputStreamReader(getClass().getResourceAsStream(path)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
