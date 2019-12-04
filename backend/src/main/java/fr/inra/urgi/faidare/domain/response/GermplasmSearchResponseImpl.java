package fr.inra.urgi.faidare.domain.response;

import fr.inra.urgi.faidare.domain.brapi.v1.response.BrapiMetadata;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.datadiscovery.data.Facet;
import fr.inra.urgi.faidare.domain.datadiscovery.data.FacetImpl;
import fr.inra.urgi.faidare.domain.datadiscovery.response.GermplasmSearchResponse;

import java.util.List;

public class GermplasmSearchResponseImpl extends ApiListResponseImpl<GermplasmVO> implements GermplasmSearchResponse {

    private final List<FacetImpl> facets;

    public GermplasmSearchResponseImpl(BrapiMetadata metadata, List<GermplasmVO> result, List<FacetImpl> facets) {
        super(metadata, result);
        this.facets = facets;
    }

    @Override
    public List<? extends Facet> getFacets() {
        return facets;
    }
}
