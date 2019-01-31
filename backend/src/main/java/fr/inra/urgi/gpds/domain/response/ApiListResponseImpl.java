package fr.inra.urgi.gpds.domain.response;

import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiData;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.gpds.domain.brapi.v1.response.BrapiMetadata;

import java.util.List;

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
class ApiListResponseImpl<T> extends ApiResponseImpl<BrapiData<T>> implements BrapiListResponse<T> {

    ApiListResponseImpl(BrapiMetadata metadata, List<T> result) {
        super(metadata, new BrapiDataImpl<>(result));
    }

    /**
     * @author gcornut
     */
    public static class BrapiDataImpl<T> implements BrapiData<T> {

        private final List<T> data;

        BrapiDataImpl(List<T> data) {
            this.data = data;
        }

        @Override
        public List<T> getData() {
            return data;
        }

    }
}
