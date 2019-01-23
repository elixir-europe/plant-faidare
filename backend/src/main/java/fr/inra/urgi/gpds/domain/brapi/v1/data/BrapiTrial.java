package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Trials/GetTrialById.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Trials/ListTrialSummaries.md
 */
public interface BrapiTrial extends Serializable {

    // Trial
    @JsonView(JSONView.BrapiFields.class)
    String getTrialDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getTrialName();

    @JsonView(JSONView.BrapiFields.class)
    String getTrialType();

    @JsonView(JSONView.BrapiFields.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date getEndDate();

    @JsonView(JSONView.BrapiFields.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date getStartDate();

    @JsonView(JSONView.BrapiFields.class)
    Boolean getActive();

    @JsonView(JSONView.BrapiFields.class)
    String getProgramDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getProgramName();

    // Authorship
    @JsonView(JSONView.BrapiFields.class)
    BrapiTrialDatasetAuthorship getDatasetAuthorship();

    // Contacts
    @JsonView(JSONView.BrapiFields.class)
    List<BrapiContact> getContacts();

    // Studies
    @JsonView(JSONView.BrapiFields.class)
    List<BrapiTrialStudy> getStudies();

    // Additional info
    @JsonView(JSONView.BrapiFields.class)
    BrapiAdditionalInfo getAdditionalInfo();

}
