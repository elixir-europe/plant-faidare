package fr.inra.urgi.faidare.web.thymeleaf;

import java.util.Collections;
import java.util.Set;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * The object factory for the {@link FaidareDialect}
 * @author JB Nizet
 */
public class FaidareExpressionFactory implements IExpressionObjectFactory {
    private static final String FAIDARE_EVALUATION_VARIABLE_NAME = "faidare";

    private static final Set<String> ALL_EXPRESSION_OBJECT_NAMES =
        Collections.singleton(FAIDARE_EVALUATION_VARIABLE_NAME);

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return ALL_EXPRESSION_OBJECT_NAMES;
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        return new FaidareExpressions(context.getLocale());
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return true;
    }
}
