package fr.inra.urgi.gpds.domain.data.impl;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiCall;

import java.util.List;

/**
 * @author gcornut
 *
 *
 */
public class CallVO implements BrapiCall {

	private String call;
	private List<String> dataTypes;
	private List<String> methods;
	private List<String> versions;

	@Override
	public String getCall() {
		return call;
	}

	public void setCall(String call) {
		this.call = call;
	}

	@Override
	public List<String> getDatatypes() {
		return dataTypes;
	}

    @Override
    public List<String> getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(List<String> datatypes) {
        this.dataTypes = datatypes;
    }

    @Override
	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

    @Override
    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }
}
