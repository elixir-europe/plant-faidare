package fr.inra.urgi.faidare.repository.file;

import fr.inra.urgi.faidare.domain.datadiscovery.data.DataSource;

import java.util.Collection;

/**
 * @author gcornut
 */
public interface DataSourceRepository {

    DataSource getByUri(String uri);

    Collection<DataSource> listAll();

}
