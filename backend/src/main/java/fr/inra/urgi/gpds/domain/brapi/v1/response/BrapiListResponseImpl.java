package fr.inra.urgi.gpds.domain.brapi.v1.response;

/**
 * bean for general paginated response structure for breeding API
 *
 * @author gcornut
 *
 <code>
 {
   "metadata": {
     "data": []
	},
   "result" : {}
 }
 </code>
 *
 *
 */
class BrapiListResponseImpl<T> extends BrapiResponseImpl<BrapiData<T>> implements BrapiListResponse<T> {

	BrapiListResponseImpl(BrapiMetadata metadata, BrapiData<T> result) {
		super(metadata, result);
	}

}
