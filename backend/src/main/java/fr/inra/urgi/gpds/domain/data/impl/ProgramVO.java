package fr.inra.urgi.gpds.domain.data.impl;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiProgram;
import fr.inra.urgi.gpds.domain.data.GnpISInternal;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Id;

import java.util.List;

/**
 * ProgramVO extending the official BreedingAPI specs with specific fields
 *
 * @author gcornut
 */
@Document(type = "program")
public class ProgramVO implements GnpISInternal, BrapiProgram {

    @Id
    private String programDbId;

    private String name;
    private String abbreviation;

    // This field is required to be non null in BRAVA
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String leadPerson = "";

    // This field is required to be non null in BRAVA
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private String objective = "";

    // GnpIS specific fields
    private Long groupId;
    private List<Long> speciesGroup;

    @Override
    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public String getLeadPerson() {
        return leadPerson;
    }

    public void setLeadPerson(String leadPerson) {
        this.leadPerson = leadPerson;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    @Override
    public String getProgramDbId() {
        return programDbId;
    }

    public void setProgramDbId(String programDbId) {
        this.programDbId = programDbId;
    }

    @Override
    public List<Long> getSpeciesGroup() {
        return speciesGroup;
    }

    public void setSpeciesGroup(List<Long> speciesGroup) {
        this.speciesGroup = speciesGroup;
    }

    @Override
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

}
