package fr.inrae.urgi.faidare.domain;

import java.util.List;

public interface GermplasmCollectingInfo {
    List<? extends GermplasmInstitute> getCollectingInstitutes();

    String getCollectingMissionIdentifier();

    String getCollectingNumber();

    GermplasmCollectingSite getCollectingSite();
}
