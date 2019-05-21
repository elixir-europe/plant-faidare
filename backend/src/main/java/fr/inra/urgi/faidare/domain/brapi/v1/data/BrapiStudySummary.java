package fr.inra.urgi.faidare.domain.brapi.v1.data;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;

/**
 * @author gcornut
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/SearchStudies.md
 * @link https://github.com/plantbreeding/API/blob/master/Specification/Studies/ListStudySummaries.md
 */
public interface BrapiStudySummary extends BrapiStudy {

    // Location
    @JsonView(JSONView.BrapiFields.class)
    String getLocationDbId();

    @JsonView(JSONView.BrapiFields.class)
    String getLocationName();

}
