package fr.inra.urgi.gpds.api.gnpis.v1;

import fr.inra.urgi.gpds.domain.response.PaginatedList;
import fr.inra.urgi.gpds.domain.xref.XRefDocumentSearchCriteria;
import fr.inra.urgi.gpds.domain.xref.XRefDocumentVO;
import fr.inra.urgi.gpds.repository.es.XRefDocumentRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Imported and adapted from unified-interface legacy
 */
@Api(tags = {"GnpIS API"}, description = "Extended GnpIS API")
@RestController
public class XRefDocumentController {

    private final XRefDocumentRepository repository;

    @Autowired
    public XRefDocumentController(XRefDocumentRepository repository) {
        this.repository = repository;
    }

    @ApiOperation("Find xref documents")
    @GetMapping(value = "/gnpis/v1/xref/documentbyfulltextid")
    public PaginatedList<XRefDocumentVO> documentByFullTextId(
        @RequestParam(required = false, value = "entry_type") String entryType,
        @RequestParam(required = false) List<String> linkedRessourcesID
    ) {
        XRefDocumentSearchCriteria criteria = new XRefDocumentSearchCriteria();
        criteria.setEntryType(entryType);
        criteria.setLinkedRessourcesID(linkedRessourcesID);
	return repository.find(criteria);
    }

}
