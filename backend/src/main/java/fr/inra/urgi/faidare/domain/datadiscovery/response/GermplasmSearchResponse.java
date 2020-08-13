package fr.inra.urgi.faidare.domain.datadiscovery.response;

import com.fasterxml.jackson.annotation.JsonView;
import fr.inra.urgi.faidare.domain.JSONView;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiData;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiListResponse;
import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiMetadata;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.datadiscovery.data.Facet;

import java.util.List;

public interface GermplasmSearchResponse extends BrapiListResponse<GermplasmVO> {

    @Override
    @JsonView(JSONView.GnpISFields.class)
    BrapiMetadata getMetadata();

    @Override
    @JsonView(JSONView.GnpISFields.class)
    BrapiData<GermplasmVO> getResult();

    @JsonView(JSONView.GnpISFields.class)
    List<? extends Facet> getFacets();
}
