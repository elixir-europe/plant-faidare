package fr.inrae.urgi.faidare.domain;

import java.util.Objects;

public class PuiNameValueVO {

    private String name;

    private String pui;

    private String value;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPui() {
        return pui;
    }

    public void setPui(String pui) {
        this.pui = pui;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PuiNameValueVO that = (PuiNameValueVO) o;
        return Objects.equals(pui, that.pui);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pui);
    }
}
