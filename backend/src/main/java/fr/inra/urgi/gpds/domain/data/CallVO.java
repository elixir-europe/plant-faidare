package fr.inra.urgi.gpds.domain.data;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiCall;

import java.util.Set;

import static fr.inra.urgi.gpds.api.brapi.v1.CallsController.DEFAULT_BRAPI_VERSIONS;
import static fr.inra.urgi.gpds.api.brapi.v1.CallsController.DEFAULT_DATA_TYPES;

/**
 * @author gcornut
 */
public class CallVO implements BrapiCall {

    private final String call;
    private Set<String> datatypes = DEFAULT_DATA_TYPES;
    private Set<String> versions = DEFAULT_BRAPI_VERSIONS;
    private Set<String> methods;

    public CallVO(String call) {
        this.call = call;
    }

    @Override
    public String getCall() {
        return call;
    }

    @Override
    public Set<String> getDatatypes() {
        return datatypes;
    }

    @Override
    public Set<String> getMethods() {
        return methods;
    }

    @Override
    public Set<String> getVersions() {
        return versions;
    }

    public void setDatatypes(Set<String> datatypes) {
        this.datatypes = datatypes;
    }

    public void setMethods(Set<String> methods) {
        this.methods = methods;
    }

    public void setVersions(Set<String> versions) {
        this.versions = versions;
    }
}
