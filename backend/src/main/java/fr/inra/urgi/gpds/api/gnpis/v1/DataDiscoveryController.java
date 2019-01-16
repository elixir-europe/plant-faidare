package fr.inra.urgi.gpds.api.gnpis.v1;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcornut
 *
 *         Copyright (c) 2019 INRA URGI
 */
@Api(tags = { "GnpIS API", "Data discovery" }, description = "GnpIS API data discovery endpoint")
@RestController
@RequestMapping("/gnpis/v1/datadiscovery")
public class DataDiscoveryController {

    @ResponseBody
    @PostMapping("/suggest")
    public String calls() {
        return "ok";
    }

}
