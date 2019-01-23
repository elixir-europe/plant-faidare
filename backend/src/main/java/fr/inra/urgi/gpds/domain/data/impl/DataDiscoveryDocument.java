package fr.inra.urgi.gpds.domain.data.impl;

import fr.inra.urgi.gpds.domain.data.DataDiscovery;
import fr.inra.urgi.gpds.domain.data.DataDiscoveryViaGermplasm;
import fr.inra.urgi.gpds.domain.data.DataDiscoveryViaTrait;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Document;
import fr.inra.urgi.gpds.elasticsearch.document.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * @author gcornut
 */
@Document(type = "datadiscovery")
public class DataDiscoveryDocument
    implements Serializable, DataDiscovery, DataDiscoveryViaGermplasm,
    DataDiscoveryViaTrait {

    @Id(jsonName = "@id")
    private String uri;

    private List<String> type;
    private String identifier;
    private String name;
    private String url;
    private String description;
    private String sourceUri;

    private DataDiscoveryViaGermplasm.GermplasmSection germplasm;

    private DataDiscoveryViaTrait.TraitSection trait;

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    @Override
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String source) {
        this.sourceUri = source;
    }

    @Override
    public GermplasmSection getGermplasm() {
        return germplasm;
    }

    public void setGermplasm(GermplasmSection germplasm) {
        this.germplasm = germplasm;
    }

    @Override
    public TraitSection getTrait() {
        return trait;
    }

    public void setTrait(TraitSection trait) {
        this.trait = trait;
    }
}
