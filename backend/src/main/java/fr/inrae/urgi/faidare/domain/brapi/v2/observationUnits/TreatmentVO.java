package fr.inrae.urgi.faidare.domain.brapi.v2.observationUnits;

public class TreatmentVO {
    private String factor;   // ex: 'fertilizer', 'inoculation', 'irrigation'
    private String modality; // ex: 'low fertilizer', 'yellow rust inoculation', 'high water'

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }
}

