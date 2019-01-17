package fr.inra.urgi.gpds.domain.criteria;

import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.impl.DataDiscoveryDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.DocumentPath;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.NoDocumentMapping;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.QueryType;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;

import java.util.List;

/**
 * @author gcornut
 *
 *
 *
 */
@CriteriaForDocument(DataDiscoveryDocument.class)
public class DataDiscoveryCriteriaImpl extends PaginationCriteriaImpl implements DataDiscoveryCriteria {

	@DocumentPath({"germplasm", "cropName"})
	private List<String> crops;

	@DocumentPath({"germplasm", "germplasmList"})
	private List<String> germplasmLists;

	@DocumentPath(value = {"germplasm", "accession"}, virtualField = "suggest")
	@QueryType(MatchPhraseQueryBuilder.class)
	private List<String> accessions;

	@DocumentPath({"trait", "observationVariableIds"})
	private List<String> observationVariableIds;

	@DocumentPath("sourceUri")
	private List<String> sources;

	@DocumentPath("type")
	private List<String> types;

	/**
	 * Field used to request for facets of fields present in this criteria
	 */
	@NoDocumentMapping
	private List<String> facetFields;

	@NoDocumentMapping
	private final String sortBy = null;// = "schema:name";

	@NoDocumentMapping
	private final String sortOrder = null;// = SortOrder.ASC.name();

	@Override
	public List<String> getCrops() {
		return crops;
	}

	public void setCrops(List<String> crops) {
		this.crops = crops;
	}

	@Override
	public List<String> getGermplasmLists() {
		return germplasmLists;
	}

	public void setGermplasmLists(List<String> germplasmLists) {
		this.germplasmLists = germplasmLists;
	}

	@Override
	public List<String> getAccessions() {
		return accessions;
	}

	public void setAccessions(List<String> accessions) {
		this.accessions = accessions;
	}

	@Override
	public List<String> getObservationVariableIds() {
		return observationVariableIds;
	}

	public void setObservationVariableIds(List<String> observationVariableIds) {
		this.observationVariableIds = observationVariableIds;
	}

	@Override
	public List<String> getSources() {
		return sources;
	}

	public void setSources(List<String> sources) {
		this.sources = sources;
	}

	@Override
	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	@Override
	public List<String> getFacetFields() {
		return facetFields;
	}

	public void setFacetFields(List<String> facetFields) {
		this.facetFields = facetFields;
	}

	@Override
	public String getSortBy() {
		return sortBy;
	}

	@Override
	public String getSortOrder() {
		return sortOrder;
	}
}
