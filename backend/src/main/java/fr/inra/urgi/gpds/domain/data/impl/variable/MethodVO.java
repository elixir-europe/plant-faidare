package fr.inra.urgi.gpds.domain.data.impl.variable;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiMethod;

/**
 * @author gcornut
 *
 *
 */
public class MethodVO implements BrapiMethod {

	private String methodDbId;
	private String name;
	private String methodClass;
	private String description;
	private String formula;
	private String reference;

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getMethodClass() {
		return methodClass;
	}

	public void setMethodClass(String methodClass) {
		this.methodClass = methodClass;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	@Override
	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	@Override
	public String getMethodDbId() {
		return methodDbId;
	}

	public void setMethodDbId(String methodDbId) {
		this.methodDbId = methodDbId;
	}
}
