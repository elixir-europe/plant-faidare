package fr.inra.urgi.faidare.web.thymeleaf;

import org.springframework.stereotype.Component;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

/**
 * A thymeleaf dialect allowing to perform various tasks in the template related to Faidare
 * @author JB Nizet
 */
@Component
public class FaidareDialect extends AbstractDialect implements IExpressionObjectDialect {

    private final IExpressionObjectFactory FAIDARE_EXPRESSION_OBJECTS_FACTORY = new FaidareExpressionFactory();

    protected FaidareDialect() {
        super("faidare");
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return FAIDARE_EXPRESSION_OBJECTS_FACTORY;
    }
}
