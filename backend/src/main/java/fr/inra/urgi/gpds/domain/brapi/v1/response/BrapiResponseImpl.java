package fr.inra.urgi.gpds.domain.brapi.v1.response;

/**
 * @author gcornut
 *
 *
 */
class BrapiResponseImpl<T> implements BrapiResponse<T> {

	private final BrapiMetadata metadata;

	private final T result;

	BrapiResponseImpl(BrapiMetadata metadata, T result) {
		this.metadata = metadata;
		this.result = result;
	}

	@Override
	public T getResult() {
		return result;
	}

	@Override
	public BrapiMetadata getMetadata() {
		return metadata;
	}

}
