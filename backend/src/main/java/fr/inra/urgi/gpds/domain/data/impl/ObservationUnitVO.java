package fr.inra.urgi.gpds.domain.data.impl;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiObservation;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiObservationUnit;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiObservationUnitTreatment;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiObservationUnitXRef;
import fr.inra.urgi.gpds.domain.data.GnpISInternal;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Id;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Nested;

import java.util.List;

/**
 * @author gcornut
 *
 *
 */
@Document(type = "observationUnit")
public class ObservationUnitVO implements BrapiObservationUnit, GnpISInternal {

	@Id
	private String observationUnitDbId;
	private String observationUnitName;

	// Layout
	private String observationLevel;
	private String observationLevels;
	private String plotNumber;
	private String plantNumber;
	private String blockNumber;
	private String replicate;
	// Transiet solution, it would be better to apply this configuraiton to all
	// string fields globaly rather than doing it field by field.
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String entryType;
	// Transiet solution, it would be better to apply this configuraiton to all
	// string fields globaly rather than doing it field by field.
	@JsonSetter(nulls = Nulls.AS_EMPTY)
	private String entryNumber;
	private String X;
	private String Y;

	// XRef
	@JsonDeserialize(contentAs = ObservationUnitXRefVO.class)
	private List<BrapiObservationUnitXRef> observationUnitXref;

	// Germplasm
	private String germplasmDbId;
	private String germplasmName;

	// Study
	private String studyDbId;
	private String studyName;

	// Study location
	private String studyLocationDbId;
	private String studyLocation;

	// Program
	private String programDbId;
	private String programName;

	@JsonDeserialize(contentAs = TreatmentVO.class)
	private List<BrapiObservationUnitTreatment> treatments;

	@Nested
	@JsonDeserialize(contentAs = ObservationVO.class)
	private List<BrapiObservation> observations;

	// GnpIS specific fields
	private Long groupId;
	private List<Long> speciesGroup;

	@Override
	public List<BrapiObservationUnitXRef> getObservationUnitXref() {
		return observationUnitXref;
	}

	public void setObservationUnitXref(List<BrapiObservationUnitXRef> observationUnitXref) {
		this.observationUnitXref = observationUnitXref;
	}

	@Override
	public String getProgramDbId() {
		return programDbId;
	}

	public void setProgramDbId(String programDbId) {
		this.programDbId = programDbId;
	}

	@Override
	public String getObservationUnitDbId() {
		return observationUnitDbId;
	}

	public void setObservationUnitDbId(String observationUnitDbId) {
		this.observationUnitDbId = observationUnitDbId;
	}

	@Override
	public String getObservationUnitName() {
		return observationUnitName;
	}

	public void setObservationUnitName(String observationUnitName) {
		this.observationUnitName = observationUnitName;
	}

	@Override
	public String getObservationLevel() {
		return observationLevel;
	}

	public void setObservationLevel(String observationLevel) {
		this.observationLevel = observationLevel;
	}

	@Override
	public String getObservationLevels() {
		return observationLevels;
	}

	public void setObservationLevels(String observationLevels) {
		this.observationLevels = observationLevels;
	}

	@Override
	public String getPlotNumber() {
		return plotNumber;
	}

	public void setPlotNumber(String plotNumber) {
		this.plotNumber = plotNumber;
	}

	@Override
	public String getPlantNumber() {
		return plantNumber;
	}

	public void setPlantNumber(String plantNumber) {
		this.plantNumber = plantNumber;
	}

	@Override
	public String getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}

	@Override
	public String getReplicate() {
		return replicate;
	}

	public void setReplicate(String replicate) {
		this.replicate = replicate;
	}

	@Override
	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	@Override
	public String getEntryNumber() {
		return entryNumber;
	}

	public void setEntryNumber(String entryNumber) {
		this.entryNumber = entryNumber;
	}

	@Override
	public String getGermplasmDbId() {
		return germplasmDbId;
	}

	public void setGermplasmDbId(String germplasmDbId) {
		this.germplasmDbId = germplasmDbId;
	}

	@Override
	public String getGermplasmName() {
		return germplasmName;
	}

	public void setGermplasmName(String germplasmName) {
		this.germplasmName = germplasmName;
	}

	@Override
	public String getStudyDbId() {
		return studyDbId;
	}

	public void setStudyDbId(String studyDbId) {
		this.studyDbId = studyDbId;
	}

	@Override
	public String getStudyName() {
		return studyName;
	}

	public void setStudyName(String studyName) {
		this.studyName = studyName;
	}

	@Override
	public String getStudyLocationDbId() {
		return studyLocationDbId;
	}

	public void setStudyLocationDbId(String studyLocationDbId) {
		this.studyLocationDbId = studyLocationDbId;
	}

	@Override
	public String getStudyLocation() {
		return studyLocation;
	}

	public void setStudyLocation(String studyLocation) {
		this.studyLocation = studyLocation;
	}

	@Override
	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	@Override
	public String getX() {
		return X;
	}

	public void setX(String x) {
		X = x;
	}

	@Override
	public String getY() {
		return Y;
	}

	public void setY(String y) {
		Y = y;
	}

	@Override
	public List<BrapiObservationUnitTreatment> getTreatments() {
		return treatments;
	}

	public void setTreatments(List<BrapiObservationUnitTreatment> treatments) {
		this.treatments = treatments;
	}

	@Override
	public List<BrapiObservation> getObservations() {
		return observations;
	}

	public void setObservations(List<BrapiObservation> observations) {
		this.observations = observations;
	}

	@Override
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Override
	public List<Long> getSpeciesGroup() {
		return speciesGroup;
	}

	public void setSpeciesGroup(List<Long> speciesGroup) {
		this.speciesGroup = speciesGroup;
	}

}
