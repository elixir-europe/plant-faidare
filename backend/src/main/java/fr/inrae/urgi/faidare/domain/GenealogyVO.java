package fr.inrae.urgi.faidare.domain;
import java.util.List;

public class GenealogyVO {

    private String crossingPlan;

    private String crossingYear;

    private String familyCode;

    private String firstParentName;

    private String firstParentPUI;

    private String firstParentType;

    private String secondParentName;

    private String secondParentPUI;

    private String secondParentType;

    private List<PuiNameValueVO> sibblings;


    public String getCrossingPlan() {
        return crossingPlan;
    }

    public void setCrossingPlan(String crossingPlan) {
        this.crossingPlan = crossingPlan;
    }

    public String getCrossingYear() {
        return crossingYear;
    }

    public void setCrossingYear(String crossingYear) {
        this.crossingYear = crossingYear;
    }

    public String getFamilyCode() {
        return familyCode;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    public String getFirstParentName() {
        return firstParentName;
    }

    public void setFirstParentName(String firstParentName) {
        this.firstParentName = firstParentName;
    }

    public String getFirstParentPUI() {
        return firstParentPUI;
    }

    public void setFirstParentPUI(String firstParentPUI) {
        this.firstParentPUI = firstParentPUI;
    }

    public String getFirstParentType() {
        return firstParentType;
    }

    public void setFirstParentType(String firstParentType) {
        this.firstParentType = firstParentType;
    }

    public String getSecondParentName() {
        return secondParentName;
    }

    public void setSecondParentName(String secondParentName) {
        this.secondParentName = secondParentName;
    }

    public String getSecondParentPUI() {
        return secondParentPUI;
    }

    public void setSecondParentPUI(String secondParentPUI) {
        this.secondParentPUI = secondParentPUI;
    }

    public String getSecondParentType() {
        return secondParentType;
    }

    public void setSecondParentType(String secondParentType) {
        this.secondParentType = secondParentType;
    }

    public List<PuiNameValueVO> getSibblings() {
        return sibblings;
    }

    public void setSibblings(List<PuiNameValueVO> sibblings) {
        this.sibblings = sibblings;
    }
}
