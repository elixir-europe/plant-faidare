package fr.inra.urgi.gpds.domain.brapi.v1.response;

/**
 * bean for general paginated response structure for breeding API
 *
 * @author gcornut
 *
 * <code>
 * {
 * "metadata": {
 * "data": []
 * },
 * "result" : {}
 * }
 * </code>
 */
public interface BrapiListResponse<T> extends BrapiResponse<BrapiData<T>> {
}
