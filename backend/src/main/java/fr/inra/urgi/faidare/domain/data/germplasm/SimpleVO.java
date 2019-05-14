package fr.inra.urgi.faidare.domain.data.germplasm;

import java.io.Serializable;

/**
 * @author C. Michotey
 */
public class SimpleVO implements Serializable, PuiNameValue {

    private static final long serialVersionUID = 3440255005695104200L;

    private String pui;
    private String name;
    private String value;

    public SimpleVO() {
    }

    public SimpleVO(String pui, String name, String value) {
        this.pui = pui;
        this.name = name;
        this.value = value;
    }

    @Override
    public String getPui() {
        return pui;
    }

    public void setPui(String pui) {
        this.pui = pui;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
