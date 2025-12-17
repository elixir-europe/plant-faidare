package fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits;

import fr.inrae.urgi.faidare.config.ElasticSearchConfig;
import fr.inrae.urgi.faidare.domain.ExternalReferencesVO;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;


@Import({ElasticSearchConfig.class})
@Document(
    indexName = "#{@faidarePropertiesBean.getAliasName('observationUnit', 0L)}",
    createIndex = false
)
public class ObservationUnitV2VO {

    private String observationUnitDbId;
    private String observationUnitName;
    //private Map<String, Object> additionalInfo;
    //private List<ExternalReferencesVO> externalReferences;
    private String germplasmDbId;
    private String germplasmName;
    private ObservationUnitPositionVO observationUnitPosition;
    private String blockNumber;
    private String accessionNumber;
    private String plantNumber;
    private String plotNumber;
    private String replicate;
    private String studyLocationDbId;
    private String studyLocation;
    private String programDbId;
    private String programName;
    private String seedLotDbId;
    private String seedLotName;
    private String studyDbId;
    private String studyName;
    private List<TreatmentVO> treatments;
    private String taxonScientificName;
    private String germplasmGenus;
    private GermplasmCollection germplasmCollections;
    private GermplasmPanel germplasmPanels;
    private String trialDbId;
    private String trialName;
    @Id
    private String _id;

    public String getObservationUnitDbId() {
        return observationUnitDbId;
    }

    public void setObservationUnitDbId(String observationUnitDbId) {
        this.observationUnitDbId = observationUnitDbId;
    }

    public String getObservationUnitName() {
        return observationUnitName;
    }

    public void setObservationUnitName(String observationUnitName) {
        this.observationUnitName = observationUnitName;
    }

    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public String getGermplasmName() {
        return germplasmName;
    }

    public void setGermplasmName(String germplasmName) {
        this.germplasmName = germplasmName;
    }

    public ObservationUnitPositionVO getObservationUnitPosition() {
        return observationUnitPosition;
    }

    public void setObservationUnitPosition(ObservationUnitPositionVO observationUnitPosition) {
        this.observationUnitPosition = observationUnitPosition;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getPlantNumber() {
        return plantNumber;
    }

    public void setPlantNumber(String plantNumber) {
        this.plantNumber = plantNumber;
    }

    public String getPlotNumber() {
        return plotNumber;
    }

    public void setPlotNumber(String plotNumber) {
        this.plotNumber = plotNumber;
    }

    public String getReplicate() {
        return replicate;
    }

    public void setReplicate(String replicate) {
        this.replicate = replicate;
    }

    public String getStudyLocationDbId() {
        return studyLocationDbId;
    }

    public void setStudyLocationDbId(String studyLocationDbId) {
        this.studyLocationDbId = studyLocationDbId;
    }

    public String getStudyLocation() {
        return studyLocation;
    }

    public void setStudyLocation(String studyLocation) {
        this.studyLocation = studyLocation;
    }

    public String getProgramDbId() {
        return programDbId;
    }

    public void setProgramDbId(String programDbId) {
        this.programDbId = programDbId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getSeedLotDbId() {
        return seedLotDbId;
    }

    public void setSeedLotDbId(String seedLotDbId) {
        this.seedLotDbId = seedLotDbId;
    }

    public String getSeedLotName() {
        return seedLotName;
    }

    public void setSeedLotName(String seedLotName) {
        this.seedLotName = seedLotName;
    }

    public String getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(String studyDbId) {
        this.studyDbId = studyDbId;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public List<TreatmentVO> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<TreatmentVO> treatments) {
        this.treatments = treatments;
    }

    public String getTaxonScientificName() {
        return taxonScientificName;
    }

    public void setTaxonScientificName(String taxonScientificName) {
        this.taxonScientificName = taxonScientificName;
    }

    public String getGermplasmGenus() {
        return germplasmGenus;
    }

    public void setGermplasmGenus(String germplasmGenus) {
        this.germplasmGenus = germplasmGenus;
    }

    public GermplasmCollection getGermplasmCollections() {
        return germplasmCollections;
    }

    public void setGermplasmCollections(GermplasmCollection germplasmCollections) {
        this.germplasmCollections = germplasmCollections;
    }

    public GermplasmPanel getGermplasmPanels() {
        return germplasmPanels;
    }

    public void setGermplasmPanels(GermplasmPanel germplasmPanels) {
        this.germplasmPanels = germplasmPanels;
    }

    public String getTrialDbId() {
        return trialDbId;
    }

    public void setTrialDbId(String trialDbId) {
        this.trialDbId = trialDbId;
    }

    public String getTrialName() {
        return trialName;
    }

    public void setTrialName(String trialName) {
        this.trialName = trialName;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
