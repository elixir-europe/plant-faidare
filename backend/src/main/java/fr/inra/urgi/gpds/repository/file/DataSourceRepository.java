package fr.inra.urgi.gpds.repository.file;

import fr.inra.urgi.gpds.domain.datadiscovery.data.DataSource;

import java.util.Collection;

/**
 * @author gcornut
 */
public interface DataSourceRepository {

    DataSource getByUri(String uri);

    Collection<DataSource> listAll();

}
