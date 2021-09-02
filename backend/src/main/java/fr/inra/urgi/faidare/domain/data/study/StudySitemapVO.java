package fr.inra.urgi.faidare.domain.data.study;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiAdditionalInfo;
import fr.inra.urgi.faidare.domain.brapi.v1.data.BrapiStudySummary;
import fr.inra.urgi.faidare.domain.data.GnpISInternal;
import fr.inra.urgi.faidare.domain.jsonld.data.HasURI;
import fr.inra.urgi.faidare.domain.jsonld.data.HasURL;
import fr.inra.urgi.faidare.domain.jsonld.data.IncludedInDataCatalog;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Document;
import fr.inra.urgi.faidare.elasticsearch.document.annotation.Id;

/**
 * A minimal view of a study containing only its ID, used to generate sitemaps
 */
@Document(type = "study", includedFields = "studyDbId")
public class StudySitemapVO {

    @Id
    private String studyDbId;

    public StudySitemapVO() {
    }

    public StudySitemapVO(String studyDbId) {
        this.studyDbId = studyDbId;
    }

    public String getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(String studyDbId) {
        this.studyDbId = studyDbId;
    }
}
