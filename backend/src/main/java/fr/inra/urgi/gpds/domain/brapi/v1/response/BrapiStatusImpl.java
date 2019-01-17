package fr.inra.urgi.gpds.domain.brapi.v1.response;

/**
 * @author gcornut
 *
 *
 */
class BrapiStatusImpl implements BrapiStatus {

	private final String name;
	private final String code;

	BrapiStatusImpl(String name, String code) {
		this.name = name;
		this.code = code;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getCode() {
		return code;
	}
}
