package fr.inra.urgi.faidare.domain.brapi.v1.criteria;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Programs/ProgramSearch.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Programs/ListPrograms.md
 */
public interface BrapiProgramCriteria extends BrapiPaginationCriteria {

    String getProgramDbId();

    String getName();

    String getAbbreviation();

    String getObjective();

    String getLeadPerson();

}
