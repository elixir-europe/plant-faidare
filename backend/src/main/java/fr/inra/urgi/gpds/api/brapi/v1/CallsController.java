package fr.inra.urgi.gpds.api.brapi.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gcornut
 *
 *         Copyright (c) 2019 INRA URGI
 */
@Api(tags = { "Breeding API" }, description = "BrAPI endpoints")
@RestController
@RequestMapping("/brapi/v1/calls")
public class CallsController {

    /**
	 * @link https://github.com/plantbreeding/API/blob/master/Specification/Calls/Calls.md
	 */
	@ApiOperation("List implemented Breeding API calls")
	@ResponseBody
    @GetMapping
	public String calls() {
        return "ok";
	}

}
