package fr.inrae.urgi.faidare.web.thymeleaf;

import fr.inrae.urgi.faidare.config.FaidareProperties;
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

    private final IExpressionObjectFactory faidareExpressionFactory;

    public FaidareDialect(FaidareProperties faidareProperties) {
        super("faidare");
        faidareExpressionFactory = new FaidareExpressionFactory(faidareProperties.getSearchUrl());
    }

    @Override
    public IExpressionObjectFactory getExpressionObjectFactory() {
        return faidareExpressionFactory;
    }
}
