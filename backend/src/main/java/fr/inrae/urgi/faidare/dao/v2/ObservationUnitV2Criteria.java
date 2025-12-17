package fr.inrae.urgi.faidare.dao.v2;

import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.GermplasmCollection;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.GermplasmPanel;
import fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits.TreatmentVO;

import java.util.List;

public class ObservationUnitV2Criteria {
    private List<String> observationUnitDbId;
    private List<String> observationUnitName;
    private List<String> germplasmDbId;
    private List<String> germplasmName;
    //private List<ObservationUnitPositionVO> observationUnitPositions;
    private List<String> blockNumber;
    private List<String> accessionNumber;
    private List<String> plantNumber;
    private List<String> plotNumber;
    private List<String> replicate;
    private List<String> locationDbId;
    private List<String> locationName;
    private List<String> programDbId;
    private List<String> programName;
    private List<String> seedLotDbId;
    private List<String> seedLotName;
    private List<String> studyDbId;
    private List<String> studyName;
    private List<TreatmentVO> treatments;
    private List<String> taxonScientificName;
    private List<String> germplasmGenus;
    private List<GermplasmCollection> germplasmCollection;
    private List<GermplasmPanel> germplasmPanel;
    private List<String> trialDbId;
    private List<String> trialName;
    private Integer page;
    private Integer pageSize;


    public List<String> getObservationUnitDbId() {
        return observationUnitDbId;
    }

    public void setObservationUnitDbId(List<String> observationUnitDbId) {
        this.observationUnitDbId = observationUnitDbId;
    }

    public List<String> getObservationUnitName() {
        return observationUnitName;
    }

    public void setObservationUnitName(List<String> observationUnitName) {
        this.observationUnitName = observationUnitName;
    }

    public List<String> getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(List<String> germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public List<String> getGermplasmName() {
        return germplasmName;
    }

    public void setGermplasmName(List<String> germplasmName) {
        this.germplasmName = germplasmName;
    }

//    public List<ObservationUnitPositionVO> getObservationUnitPositions() {
//        return observationUnitPositions;
//    }
//
//    public void setObservationUnitPositions(List<ObservationUnitPositionVO> observationUnitPositions) {
//        this.observationUnitPositions = observationUnitPositions;
//    }

    public List<String> getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(List<String> blockNumber) {
        this.blockNumber = blockNumber;
    }

    public List<String> getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(List<String> accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public List<String> getPlantNumber() {
        return plantNumber;
    }

    public void setPlantNumber(List<String> plantNumber) {
        this.plantNumber = plantNumber;
    }

    public List<String> getPlotNumber() {
        return plotNumber;
    }

    public void setPlotNumber(List<String> plotNumber) {
        this.plotNumber = plotNumber;
    }

    public List<String> getReplicate() {
        return replicate;
    }

    public void setReplicate(List<String> replicate) {
        this.replicate = replicate;
    }

    public List<String> getLocationDbId() {
        return locationDbId;
    }

    public void setLocationDbId(List<String> locationDbId) {
        this.locationDbId = locationDbId;
    }

    public List<String> getLocationName() {
        return locationName;
    }

    public void setLocationName(List<String> locationName) {
        this.locationName = locationName;
    }

    public List<String> getProgramDbId() {
        return programDbId;
    }

    public void setProgramDbId(List<String> programDbId) {
        this.programDbId = programDbId;
    }

    public List<String> getProgramName() {
        return programName;
    }

    public void setProgramName(List<String> programName) {
        this.programName = programName;
    }

    public List<String> getSeedLotDbId() {
        return seedLotDbId;
    }

    public void setSeedLotDbId(List<String> seedLotDbId) {
        this.seedLotDbId = seedLotDbId;
    }

    public List<String> getSeedLotName() {
        return seedLotName;
    }

    public void setSeedLotName(List<String> seedLotName) {
        this.seedLotName = seedLotName;
    }

    public List<String> getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(List<String> studyDbId) {
        this.studyDbId = studyDbId;
    }

    public List<String> getStudyName() {
        return studyName;
    }

    public void setStudyName(List<String> studyName) {
        this.studyName = studyName;
    }

    public List<TreatmentVO> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<TreatmentVO> treatments) {
        this.treatments = treatments;
    }

    public List<String> getTaxonScientificName() {
        return taxonScientificName;
    }

    public void setTaxonScientificName(List<String> taxonScientificName) {
        this.taxonScientificName = taxonScientificName;
    }

    public List<String> getGermplasmGenus() {
        return germplasmGenus;
    }

    public void setGermplasmGenus(List<String> germplasmGenus) {
        this.germplasmGenus = germplasmGenus;
    }

    public List<GermplasmCollection> getGermplasmCollection() {
        return germplasmCollection;
    }

    public void setGermplasmCollection(List<GermplasmCollection> germplasmCollection) {
        this.germplasmCollection = germplasmCollection;
    }

    public List<GermplasmPanel> getGermplasmPanel() {
        return germplasmPanel;
    }

    public void setGermplasmPanel(List<GermplasmPanel> germplasmPanel) {
        this.germplasmPanel = germplasmPanel;
    }

    public List<String> getTrialDbId() {
        return trialDbId;
    }

    public void setTrialDbId(List<String> trialDbId) {
        this.trialDbId = trialDbId;
    }

    public List<String> getTrialName() {
        return trialName;
    }

    public void setTrialName(List<String> trialName) {
        this.trialName = trialName;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
