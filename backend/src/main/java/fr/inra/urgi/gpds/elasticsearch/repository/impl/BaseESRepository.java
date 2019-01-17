package fr.inra.urgi.gpds.elasticsearch.repository.impl;

import fr.inra.urgi.gpds.domain.criteria.base.PaginationCriteria;
import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.elasticsearch.ESRequestFactory;
import fr.inra.urgi.gpds.elasticsearch.repository.ESFindRepository;
import fr.inra.urgi.gpds.elasticsearch.repository.ESGetByIdRepository;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * Generic ElasticSearch repository of document providing a search and a get by id.
 *
 * @author gcornut
 *
 *
 */
public class BaseESRepository<C extends PaginationCriteria, VO>
		implements ESGetByIdRepository<VO>, ESFindRepository<C, VO> {

	private final ESGetByIdRepository<VO> getByIdRepository;
	private final ESFindRepository<C, VO> findRepository;

	public BaseESRepository(
	    RestHighLevelClient client,
        ESRequestFactory requestFactory,
        Class<VO> voClass
    ) {
        getByIdRepository = new ESGenericGetByIdRepository<>(client, requestFactory, voClass);
		findRepository = new ESGenericFindRepository<>(client, requestFactory, voClass);
	}

	@Override
	public VO getById(String id) {
		return getByIdRepository.getById(id);
	}

	@Override
	public PaginatedList<VO> find(C criteria) {
		return findRepository.find(criteria);
	}

}
