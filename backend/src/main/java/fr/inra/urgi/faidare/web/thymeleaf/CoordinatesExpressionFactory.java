package fr.inra.urgi.faidare.web.thymeleaf;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.extras.java8time.expression.Temporals;

/**
 * The object factory for the {@link CoordinatesDialect}
 * @author JB Nizet
 */
public class CoordinatesExpressionFactory implements IExpressionObjectFactory {
    private static final String COORDINATES_EVALUATION_VARIABLE_NAME = "coordinates";

    private static final Set<String> ALL_EXPRESSION_OBJECT_NAMES =
        Collections.singleton(COORDINATES_EVALUATION_VARIABLE_NAME);

    @Override
    public Set<String> getAllExpressionObjectNames() {
        return ALL_EXPRESSION_OBJECT_NAMES;
    }

    @Override
    public Object buildObject(IExpressionContext context, String expressionObjectName) {
        return new Coordinates(context.getLocale());
    }

    @Override
    public boolean isCacheable(String expressionObjectName) {
        return true;
    }
}
