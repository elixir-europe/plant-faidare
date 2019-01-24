package fr.inra.urgi.gpds.domain.data.germplasm;

import java.io.Serializable;

/**
 * @author C. Michotey
 */
public class CollPopVO implements Serializable, CollPop {

    private static final long serialVersionUID = 4254257487619614493L;

    private Long id;
    private String name;
    private String type;
    private SimpleVO germplasmRef;
    private Integer germplasmCount;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public SimpleVO getGermplasmRef() {
        return germplasmRef;
    }

    public void setGermplasmRef(SimpleVO germplasmRef) {
        this.germplasmRef = germplasmRef;
    }

    @Override
    public Integer getGermplasmCount() {
        return germplasmCount;
    }

    public void setGermplasmCount(Integer germplasmCount) {
        this.germplasmCount = germplasmCount;
    }

}
