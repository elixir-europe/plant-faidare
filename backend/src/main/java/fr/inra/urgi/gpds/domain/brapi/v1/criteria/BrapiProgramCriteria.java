package fr.inra.urgi.gpds.domain.brapi.v1.criteria;

/**
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Programs/ProgramSearch.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Programs/ListPrograms.md
 * @author gcornut
 *
 *
 */
public interface BrapiProgramCriteria extends BrapiPaginationCriteria {

	String getProgramDbId();

	String getName();

	String getAbbreviation();

	String getObjective();

	String getLeadPerson();

}
