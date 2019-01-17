package fr.inra.urgi.gpds.elasticsearch.repository;

/**
 * Interface representing an ElasticSearch repository providing a get document
 * by id method.
 *
 * @author gcornut
 *
 *
 */
public interface ESGetByIdRepository<VO> {

	/**
	 * Get ES document by identifier
	 */
	VO getById(String id);

}
