package fr.inra.urgi.faidare.domain.response;

import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiStatus;

/**
 * @author gcornut
 */
public class ApiStatusImpl implements BrapiStatus {

    private final String name;
    private final String code;

    public ApiStatusImpl(String name, String code) {
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
