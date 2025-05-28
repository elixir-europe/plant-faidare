package fr.inrae.urgi.faidare.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface DocumentDao<D> extends ElasticsearchRepository<D, String> {

}
