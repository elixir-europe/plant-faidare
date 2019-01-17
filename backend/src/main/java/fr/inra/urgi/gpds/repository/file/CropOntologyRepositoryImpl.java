package fr.inra.urgi.gpds.repository.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import fr.inra.urgi.gpds.config.GPDSProperties;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiTrait;
import fr.inra.urgi.gpds.domain.data.impl.variable.ObservationVariableVO;
import fr.inra.urgi.gpds.domain.data.impl.variable.OntologyVO;
import fr.inra.urgi.gpds.utils.JacksonFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * CropOntology variable ontology repository
 *
 * @author gcornut
 */
@Repository
public class CropOntologyRepositoryImpl implements CropOntologyRepository {

	private final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	private static final String   ONTOLOGY_REPOSITORY_FILE_NAME = "ontology-repository.json";
	private static final Integer  CACHE_EXPIRATION_TIME = 1;
	private static final TimeUnit CACHE_EXPIRATION_TIME_UNIT = TimeUnit.HOURS;

	@Resource
	private GPDSProperties properties;

	private final ObjectMapper mapper;

	private final LoadingCache<String, OntologyVO[]> ontologyCache;
	private final LoadingCache<String, ObservationVariableVO[]> variablesByOntology;

	private CropOntologyRepositoryImpl() {
		// Cache configuration: will refresh if CACHE_EXPIRATION_TIME has passed
		// since last cache write.
		// see: https://github.com/google/guava/wiki/CachesExplained
		CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
				.expireAfterWrite(CACHE_EXPIRATION_TIME, CACHE_EXPIRATION_TIME_UNIT);

		// Ontology list cache
		ontologyCache = cacheBuilder.build(new OntologyCacheLoader());

		// Variable list by ontology cache
		variablesByOntology = cacheBuilder.build(new VariableCacheLoader());

		mapper = JacksonFactory.createPermissiveMapper();
	}

	@Override
	public List<OntologyVO> getOntologies() {
		String repositoryUrl = properties.getCropOntologyRepositoryUrl();
		try {
			return Arrays.asList(ontologyCache.get(repositoryUrl));
		} catch (ExecutionException e) {
			logger.log(Level.SEVERE,
				"Could not load ontologies from repository: "+ repositoryUrl, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ObservationVariableVO> getVariables() {
		try {
			List<ObservationVariableVO> variables = Lists.newArrayList();
			for (OntologyVO ontology : getOntologies()) {
				String ontologyKey = getOntologyKey(ontology);
				variables.addAll(Arrays.asList(variablesByOntology.get(ontologyKey)));
			}
			return variables;
		} catch (ExecutionException e) {
			logger.log(Level.SEVERE,
				"Could not load all variables.", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ObservationVariableVO> getVariablesByTraitClass(String searchedTraitClass) {
		try {
			List<ObservationVariableVO> variables = Lists.newArrayList();

			for (ObservationVariableVO variable : getVariables()) {
				BrapiTrait trait = variable.getTrait();
				if (trait != null) {
					String traitClass = trait.getTraitClass();
					if (traitClass != null && traitClass.equals(searchedTraitClass)) {
						variables.add(variable);
					}
				}
			}

			return variables;
		} catch (Exception e) {
			logger.log(Level.SEVERE,
				"Error while searching variables for trait class: " + searchedTraitClass, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public ObservationVariableVO getVariableById(String identifier) {
		try {
			for (OntologyVO ontology : getOntologies()) {
				String ontologyKey = getOntologyKey(ontology);
				for (ObservationVariableVO variable : variablesByOntology.get(ontologyKey)) {
					if (identifier.equals(variable.getObservationVariableDbId())) {
						return variable;
					}
				}
			}
			return null;
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while search for variable: "+identifier, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ObservationVariableVO> getVariableByIds(Set<String> identifiers) {
		try {
			List<ObservationVariableVO> variables = new ArrayList<>();
			for (OntologyVO ontology : getOntologies()) {
				String ontologyKey = getOntologyKey(ontology);
				for (ObservationVariableVO variable : variablesByOntology.get(ontologyKey)) {
					if (identifiers.contains(variable.getObservationVariableDbId())) {
						variables.add(variable);
					}
				}
			}
			return variables;
		} catch (Exception e) {
			logger.log(Level.SEVERE,
				"Error while search for variables: "+identifiers, e);
			throw new RuntimeException(e);
		}
	}

	private String getOntologyBaseUrl() {
		return properties.getCropOntologyRepositoryUrl().replace(ONTOLOGY_REPOSITORY_FILE_NAME, "");
	}

	private String getOntologyKey(OntologyVO ontology) {
		return ontology.getOntologyDbId() + "-" + ontology.getOntologyName();
	}

	/**
	 * Guava cache loader that loads ontologies from a JSON repository file.
	 */
	class OntologyCacheLoader extends CacheLoader<String, OntologyVO[]> {

		@Override
		public OntologyVO[] load(String repositoryUrl) throws Exception {
			return mapper.readValue(new URL(repositoryUrl), OntologyVO[].class);
		}

	}

	/**
	 * Guava cache loader that loads variables of a specific ontology from a JSON file.
	 */
	class VariableCacheLoader extends CacheLoader<String, ObservationVariableVO[]> {

		@Override
		public ObservationVariableVO[] load(String ontologyKey) throws Exception {
			String path = ontologyKey + ".json";

			URL url = new URL(getOntologyBaseUrl() + path);

			// Read from URL
			return mapper.readValue(url, ObservationVariableVO[].class);
		}

	}
}
