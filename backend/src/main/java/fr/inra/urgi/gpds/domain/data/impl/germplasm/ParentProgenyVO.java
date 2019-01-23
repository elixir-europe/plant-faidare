package fr.inra.urgi.gpds.domain.data.impl.germplasm;

import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiParentProgeny;

import java.io.Serializable;


public class ParentProgenyVO implements Serializable, BrapiParentProgeny {

    private static final long serialVersionUID = -915154032692086323L;

    private String germplasmDbId;
    private String defaultDisplayName;
    private String parentType;

    @Override
    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    @Override
    public String getDefaultDisplayName() {
        return defaultDisplayName;
    }

    @Override
    public String getParentType() {
        return parentType;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public void setDefaultDisplayName(String defaultDisplayName) {
        this.defaultDisplayName = defaultDisplayName;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }
}
