package fr.inra.urgi.faidare.domain.criteria.base;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Pagination validator restricting the max result window to match elasticsearch constrains
 *
 * @author gcornut
 */
public class PaginationMaxResultValidator implements ConstraintValidator<ValidPagination, PaginationCriteriaImpl> {

    /**
     * Default constrain on elasticsearch search result window
     *
     * @link https://www.elastic.co/guide/en/elasticsearch/reference/2.4/index-modules.html
     */
    public static final long MAX_RESULT_WINDOW = 10000;

    public static final String ERROR_MAX_RESULT_WINDOW =
        "The result window (page x pageSize) cannot be over " + MAX_RESULT_WINDOW + ". " +
            "Please use an export API to download all the requested data.";

    @Override
    public void initialize(ValidPagination annotation) {
    }

    @Override
    public boolean isValid(PaginationCriteriaImpl paginationCriteria, ConstraintValidatorContext context) {
        Long pageSize = paginationCriteria.getPageSize();
        Long page = paginationCriteria.getPage();

        if (page != null && pageSize != null) {
            if (page * pageSize >= (MAX_RESULT_WINDOW - pageSize)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(ERROR_MAX_RESULT_WINDOW).addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
