package fr.inra.urgi.faidare.domain.criteria.base;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


/**
 * @author gcornut
 */
public class PaginationValidatorTest {


    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void should_Control_Nulls() {
        PaginationCriteriaImpl criteria = new PaginationCriteriaImpl();
        criteria.setPageSize(null);
        criteria.setPage(null);

        Set<ConstraintViolation<PaginationCriteria>> violations =
            validator.validate(((PaginationCriteria) criteria));
        Assertions.assertThat(violations).isNotNull().hasSize(2);
        Assertions.assertThat(violations)
            .extracting("message")
            .containsOnly(PaginationCriteriaImpl.ERROR_PAGE_NULL, PaginationCriteriaImpl.ERROR_PAGE_SIZE_NULL);
    }

    @Test
    public void should_Control_Min() {
        PaginationCriteriaImpl criteria = new PaginationCriteriaImpl();
        criteria.setPageSize(PaginationCriteriaImpl.MIN_PAGE_SIZE - 1);
        criteria.setPage(PaginationCriteriaImpl.MIN_PAGE - 1);

        Set<ConstraintViolation<PaginationCriteria>> violations =
            validator.validate(((PaginationCriteria) criteria));
        Assertions.assertThat(violations).isNotNull().hasSize(2);
        Assertions.assertThat(violations)
            .extracting("message")
            .containsOnly(PaginationCriteriaImpl.ERROR_PAGE_MIN, PaginationCriteriaImpl.ERROR_PAGE_SIZE_MIN);
    }

    @Test
    public void should_Control_Max_Size() {
        PaginationCriteriaImpl criteria = new PaginationCriteriaImpl();
        criteria.setPageSize(PaginationCriteriaImpl.MAX_PAGE_SIZE + 1);

        Set<ConstraintViolation<PaginationCriteria>> violations =
            validator.validate(((PaginationCriteria) criteria));
        Assertions.assertThat(violations).isNotNull().hasSize(1);
        Assertions.assertThat(violations)
            .extracting("message")
            .containsOnly(PaginationCriteriaImpl.ERROR_PAGE_SIZE_MAX);
    }

    @Test
    public void should_Control_Max_Result_Window() {
        PaginationCriteriaImpl criteria = new PaginationCriteriaImpl();

        criteria.setPageSize(101L);
        criteria.setPage(100L);

        Set<ConstraintViolation<PaginationCriteria>> violations;
        violations = validator.validate(((PaginationCriteria) criteria));
        Assertions.assertThat(violations).isNotNull().hasSize(1);
        Assertions.assertThat(violations)
            .extracting("message")
            .containsOnly(PaginationMaxResultValidator.ERROR_MAX_RESULT_WINDOW);
    }

    @Test
    public void should_Succeed_On_Valid_Pagination() {
        PaginationCriteriaImpl criteria = new PaginationCriteriaImpl();

        criteria.setPageSize(13L);
        criteria.setPage(11L);

        Set<ConstraintViolation<PaginationCriteria>> violations;
        violations = validator.validate(((PaginationCriteria) criteria));
        Assertions.assertThat(violations).isNotNull().isEmpty();
    }
}
