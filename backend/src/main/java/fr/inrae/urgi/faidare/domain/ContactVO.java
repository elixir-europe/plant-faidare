package fr.inrae.urgi.faidare.domain;

public class ContactVO {

    private String contactDbId;

    private String email;

    private String instituteName;

    private String name;

    private String orcid;

    private String type;


    public String getContactDbId() {
        return contactDbId;
    }

    public void setContactDbId(String contactDbId) {
        this.contactDbId = contactDbId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrcid() {
        return orcid;
    }

    public void setOrcid(String orcid) {
        this.orcid = orcid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
