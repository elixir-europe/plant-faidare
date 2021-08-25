package fr.inra.urgi.faidare.web.thymeleaf;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.extras.java8time.dialect.Java8TimeExpressionFactory;

/**
 * A thymeleaf dialect allowing to transform coordinates (latitude and longitude)
 * to degrees.
 * @author JB Nizet
 */
@Component
public class CoordinatesDialect extends AbstractDialect implements IExpressionObjectDialect {

    private final IExpressionObjectFactory COORDINATES_EXPRESSION_OBJECTS_FACTORY = new CoordinatesExpressionFactory();

    protected CoordinatesDialect() {
        super("coordinates");
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return COORDINATES_EXPRESSION_OBJECTS_FACTORY;
    }
}
