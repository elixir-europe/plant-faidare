package fr.inra.urgi.gpds.domain.data.germplasm;

import java.util.List;

/**
 * @author C. Michotey
 */
public class GenealogyVO implements java.io.Serializable, Genealogy {

    private static final long serialVersionUID = 1875115652278653927L;

    private String crossingPlan;
    private String crossingYear;
    private String familyCode;

    private String firstParentName;
    private String firstParentPUI;
    private String firstParentType;

    private String secondParentName;
    private String secondParentPUI;
    private String secondParentType;

    private List<SimpleVO> sibblings; // puid + name

    @Override
    public String getCrossingPlan() {
        return crossingPlan;
    }

    public void setCrossingPlan(String crossingPlan) {
        this.crossingPlan = crossingPlan;
    }

    @Override
    public String getCrossingYear() {
        return crossingYear;
    }

    public void setCrossingYear(String crossingYear) {
        this.crossingYear = crossingYear;
    }

    @Override
    public String getFamilyCode() {
        return familyCode;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    @Override
    public String getFirstParentName() {
        return firstParentName;
    }

    public void setFirstParentName(String firstParentName) {
        this.firstParentName = firstParentName;
    }

    @Override
    public String getFirstParentPUI() {
        return firstParentPUI;
    }

    public void setFirstParentPUI(String fisrtParentPUI) {
        this.firstParentPUI = fisrtParentPUI;
    }

    @Override
    public String getFirstParentType() {
        return firstParentType;
    }

    public void setFirstParentType(String firstParentType) {
        this.firstParentType = firstParentType;
    }

    @Override
    public String getSecondParentName() {
        return secondParentName;
    }

    public void setSecondParentName(String secondParentName) {
        this.secondParentName = secondParentName;
    }

    @Override
    public String getSecondParentPUI() {
        return secondParentPUI;
    }

    public void setSecondParentPUI(String secondParentPUI) {
        this.secondParentPUI = secondParentPUI;
    }

    @Override
    public String getSecondParentType() {
        return secondParentType;
    }

    public void setSecondParentType(String secondParentType) {
        this.secondParentType = secondParentType;
    }

    @Override
    public List<SimpleVO> getSibblings() {
        return sibblings;
    }

    public void setSibblings(List<SimpleVO> sibblings) {
        this.sibblings = sibblings;
    }

}
