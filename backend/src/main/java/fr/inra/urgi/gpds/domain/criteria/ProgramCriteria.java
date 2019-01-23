package fr.inra.urgi.gpds.domain.criteria;

import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiPaginationCriteria;
import fr.inra.urgi.gpds.domain.brapi.v1.criteria.BrapiProgramCriteria;
import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteriaImpl;
import fr.inra.urgi.gpds.domain.data.impl.ProgramVO;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.CriteriaForDocument;
import fr.inra.urgi.gpds.elasticsearch.criteria.annotation.DocumentPath;

/**
 * @author gcornut
 */
@CriteriaForDocument(ProgramVO.class)
public class ProgramCriteria extends PaginationCriteriaImpl
    implements BrapiPaginationCriteria, BrapiProgramCriteria {

    @DocumentPath("programDbId")
    private String programDbId;

    @DocumentPath("name")
    private String name;

    @DocumentPath("abbreviation")
    private String abbreviation;

    @DocumentPath("objective")
    private String objective;

    @DocumentPath("leadPerson")
    private String leadPerson;

    @Override
    public String getProgramDbId() {
        return programDbId;
    }

    public void setProgramDbId(String programDbId) {
        this.programDbId = programDbId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    @Override
    public String getLeadPerson() {
        return leadPerson;
    }

    public void setLeadPerson(String leadPerson) {
        this.leadPerson = leadPerson;
    }

}
