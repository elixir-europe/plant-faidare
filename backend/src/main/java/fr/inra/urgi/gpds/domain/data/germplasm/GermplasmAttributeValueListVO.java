package fr.inra.urgi.gpds.domain.data.germplasm;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiGermplasmAttributeValue;
import fr.inra.urgi.gpds.domain.brapi.v1.data.BrapiGermplasmAttributeValueList;
import fr.inra.urgi.gpds.domain.data.GnpISInternal;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Nested;

import java.util.List;

/**
 * @author gcornut
 */
@Document(type = "germplasmAttribute")
public class GermplasmAttributeValueListVO implements BrapiGermplasmAttributeValueList, GnpISInternal {

    private String germplasmDbId;

    @Nested
    @JsonDeserialize(contentAs = GermplasmAttributeValueVO.class)
    private List<BrapiGermplasmAttributeValue> data;

    // GnpIS specific fields
    private List<Long> speciesGroup;
    private Long groupId;

    @Override
    public String getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(String germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    @Override
    public List<BrapiGermplasmAttributeValue> getData() {
        return data;
    }

    public void setData(List<BrapiGermplasmAttributeValue> data) {
        this.data = data;
    }

    @Override
    public List<Long> getSpeciesGroup() {
        return speciesGroup;
    }

    public void setSpeciesGroup(List<Long> speciesGroup) {
        this.speciesGroup = speciesGroup;
    }

    @Override
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
