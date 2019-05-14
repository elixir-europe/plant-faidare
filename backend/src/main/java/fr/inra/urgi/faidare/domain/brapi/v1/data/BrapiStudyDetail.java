package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

import java.util.List;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/StudyDetails.md
 */
public interface BrapiStudyDetail extends BrapiStudy {

    // Study description
    @JsonView(JSONView.BrapiFields.class)
    String getStudyDescription();

    // Study last update
    @JsonView(JSONView.BrapiFields.class)
    BrapiStudyLastUpdate getLastUpdate();

    // Contacts
    @JsonView(JSONView.BrapiFields.class)
    List<BrapiContact> getContacts();

    // Location
    @JsonView(JSONView.BrapiFields.class)
    BrapiLocation getLocation();

    // Data links
    @JsonView(JSONView.BrapiFields.class)
    List<BrapiStudyDataLink> getDataLinks();

}
