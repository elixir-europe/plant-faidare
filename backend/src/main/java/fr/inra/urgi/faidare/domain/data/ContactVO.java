package fr.inra.urgi.faidare.domain.data;

import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiContact;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyDetails.md
 */
public class ContactVO implements BrapiContact {

    private String contactDbId;
    private String name;
    private String email;
    private String type;
    private String instituteName;
    private String orcid;

    @Override
    public String getContactDbId() {
        return contactDbId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getInstituteName() {
        return instituteName;
    }

    @Override
    public String getOrcid() {
        return orcid;
    }

    public void setContactDbId(String contactDbId) {
        this.contactDbId = contactDbId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public void setOrcid(String orcid) {
        this.orcid = orcid;
    }
}
