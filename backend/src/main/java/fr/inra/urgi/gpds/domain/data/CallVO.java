package fr.inra.urgi.gpds.domain.data;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiCall;

import java.util.HashSet;
import java.util.Set;

/**
 * @author gcornut
 */
public class CallVO implements BrapiCall {

    private String call;
    private Set<String> datatypes;
    private Set<String> methods;
    private Set<String> versions;

    public CallVO(String call) {
        this.call = call;
        this.datatypes = new HashSet<>();
        this.methods = new HashSet<>();
        this.versions = new HashSet<>();
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

}
