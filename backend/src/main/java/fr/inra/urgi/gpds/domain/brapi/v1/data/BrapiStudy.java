package fr.inra.urgi.gpds.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.gpds.domain.JSONView;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Generic Brapi study.
 * <p>
 * No <code>@JsonDeserialize</code> is declared here because this interface is
 * not used directly (only extended).
 * <p>
 * Extended in {@link BrapiStudySummary} and {@link BrapiStudyDetail}
 *
 * @author gcornut
 */
public interface BrapiStudy extends HasBrapiDocumentationURL {

    // Study
    @JsonView(JSONView.BrapiFields.class)
    String getStudyDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getStudyName();

    @Deprecated
    @JsonView(JSONView.BrapiFields.class)
    String getName();

    @JsonView(JSONView.BrapiFields.class)
    String getStudyType();

    @JsonView(JSONView.BrapiFields.class)
    Boolean getActive();

    @JsonView(JSONView.BrapiFields.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date getStartDate();

    @JsonView(JSONView.BrapiFields.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date getEndDate();

    // Seasons
    @JsonView(JSONView.BrapiFields.class)
    List<String> getSeasons();

    // Program
    @JsonView(JSONView.BrapiFields.class)
    String getProgramDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getProgramName();

    // Trials
    @JsonView(JSONView.BrapiFields.class)
    String getTrialName();

    @JsonView(JSONView.BrapiFields.class)
    String getTrialDbId();

    @JsonView(JSONView.BrapiFields.class)
    Set<String> getTrialDbIds();

    // Additional info
    @JsonView(JSONView.BrapiFields.class)
    BrapiAdditionalInfo getAdditionalInfo();

    @Override
    String getDocumentationURL();
}
