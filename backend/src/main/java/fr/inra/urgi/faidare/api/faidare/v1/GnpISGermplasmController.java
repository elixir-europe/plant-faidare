package fr.inra.urgi.faidare.api.faidare.v1;

import com.google.common.base.Strings;
import fr.inra.urgi.faidare.api.BadRequestException;
import fr.inra.urgi.faidare.api.NotFoundException;
import fr.inra.urgi.faidare.domain.criteria.GermplasmGETSearchCriteria;
import fr.inra.urgi.faidare.domain.criteria.GermplasmPOSTSearchCriteria;
import fr.inra.urgi.faidare.domain.data.germplasm.GermplasmVO;
import fr.inra.urgi.faidare.domain.response.PaginatedList;
import fr.inra.urgi.faidare.service.es.GermplasmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Collections;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Api(tags = {"FAIDARE API"}, description = "Extended FAIDARE API")
@RestController
@RequestMapping(value = "/faidare/v1/germplasm")
public class GnpISGermplasmController {

    private final GermplasmService germplasmService;

    @Autowired
    public GnpISGermplasmController(GermplasmService germplasmService) {
        this.germplasmService = germplasmService;
    }

    @ApiOperation(value = "Search germplasm by ID or PUI")
    @GetMapping
    public GermplasmVO get(
        @RequestParam(required = false) String id,
        @RequestParam(required = false) String pui
    ) {
        GermplasmVO germplasm = null;
        boolean hasId = !Strings.isNullOrEmpty(id);
        boolean hasPui = !Strings.isNullOrEmpty(pui);

        if (!hasId && !hasPui) {
            throw new BadRequestException("You must provide at least one identifier (PUI or ID) in the query params.");
        }

        if (hasId && !hasPui) {
            germplasm = germplasmService.getById(id);
        } else {
            GermplasmGETSearchCriteria criteria = new GermplasmGETSearchCriteria();
            criteria.setGermplasmPUI(Collections.singletonList(pui));
            if (hasId) {
                criteria.setGermplasmDbId(Collections.singletonList(id));
            }
            PaginatedList<GermplasmVO> pager = germplasmService.find(criteria);
            if (pager != null && pager.size() == 1) {
                germplasm = pager.get(0);
            }
        }

        if (germplasm == null) {
            String message = "Germplasm not found";
            if (hasId) {
                message += " for id: '" + id + "'";
            }
            if (hasPui) {
                if (hasId) message += " and";
                message += " for pui: '" + pui + "'";
            }

            throw new NotFoundException(message);
        }
        return germplasm;
    }

    /**
     * Webservice for exporting germplasm by criteria into a CSV.
     * <p>
     * See http://stackoverflow.com/questions/5673260/downloading-a-file-from-
     * spring-controllers resp.setContentType("application/csv");
     *
     * <pre>
     * resp.setContentType("application/vnd.ms-excel");
     * resp.setContentType("application/zip");
     * </pre>
     */
    @RequestMapping(value = "/csv", method = GET, produces = "text/csv")
    public FileSystemResource export(GermplasmPOSTSearchCriteria criteria, HttpServletResponse response) {
        try {
            File exportFile = germplasmService.exportCSV(criteria);
            response.setHeader("Content-Disposition", "attachment; filename=germplasm.gnpis.csv");
            return new FileSystemResource(exportFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred when exporting germplasm: " + e.getMessage() + ".", e);
        }
    }

}
